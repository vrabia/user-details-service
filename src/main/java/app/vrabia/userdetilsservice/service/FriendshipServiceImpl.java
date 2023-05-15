package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;
import app.vrabia.userdetilsservice.mappers.FriendshipMapper;
import app.vrabia.userdetilsservice.mappers.UserMapper;
import app.vrabia.userdetilsservice.model.Friendship;
import app.vrabia.userdetilsservice.model.PendingFriendship;
import app.vrabia.userdetilsservice.model.User;
import app.vrabia.userdetilsservice.repository.FriendshipRepository;
import app.vrabia.userdetilsservice.repository.PendingFriendshipRepository;
import app.vrabia.userdetilsservice.repository.UserRepository;
import app.vrabia.vrcommon.exception.ErrorCodes;
import app.vrabia.vrcommon.exception.VrabiaException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final PendingFriendshipRepository pendingFriendshipRepository;
    private final UserRepository userRepository;
    private final FriendshipMapper friendshipMapper;
    private final UserMapper userMapper;

    @Override
    public void acceptFriendship(String senderId, String receiverId) {
        log.info("Accepting friendship between {} and {}", senderId, receiverId);
        User sender = userRepository.findById(senderId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        PendingFriendship pendingFriendship = pendingFriendshipRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        Friendship friendship = friendshipMapper.pendingFriendshipToFriendship(pendingFriendship);
        friendship.setFriendsSince(LocalDate.now());

        friendshipRepository.save(friendship);
        pendingFriendshipRepository.delete(pendingFriendship);
    }

    @Override
    public void rejectOrCancelFriendship(String senderId, String receiverId) {
        log.info("Rejecting friendship between {} and {}", senderId, receiverId);
        try {
            this.deletePendingFriendship(senderId, receiverId);
        } catch (VrabiaException e) {
            this.deletePendingFriendship(receiverId, senderId);
        }
    }

    @Override
    public void sendFriendshipRequest(String senderId, String receiverId) {
        log.info("Sending friendship request from {} to {}", senderId, receiverId);
        User sender = userRepository.findById(senderId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        PendingFriendship pendingFriendship = new PendingFriendship();
        pendingFriendship.setSender(sender);
        pendingFriendship.setReceiver(receiver);

        pendingFriendshipRepository.save(pendingFriendship);
    }

    @Override
    public void removeFriendship(String userId, String friendId) {
        log.info("Removing friendship between {} and {}", userId, friendId);
        User user = userRepository.findById(userId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        Optional<Friendship> friendship = friendshipRepository.findByUser1AndUser2(user, friend);
        if (friendship.isEmpty()) {
            friendship = friendshipRepository.findByUser1AndUser2(friend, user);
        }
        friendship.ifPresent(friendshipRepository::delete);
    }

    @Override
    public List<FriendshipDTO> getFriends(String userId) {
        log.info("Getting friends of {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        List<Friendship> friendships = this.friendshipRepository.findByUser1(user);
        List<Friendship> friendships2 = this.friendshipRepository.findByUser2(user);
        friendships.addAll(friendships2);
        return friendships.stream().map(friendship -> {
            FriendshipDTO friendshipDTO = friendshipMapper.friendshipToFriendshipDTO(friendship);
            User friend = friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1();
            friendshipDTO.setFriend(userMapper.userToUserDTO(friend));
            return friendshipDTO;
        }).toList();
    }

    @Override
    public List<FriendshipDTO> getSentFriendRequests(String userId) {
        log.info("Getting sent friend requests of {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        List<PendingFriendship> sentFriendRequests = this.pendingFriendshipRepository.findBySender(user);
        return sentFriendRequests.stream().map(friendshipMapper::pendingSentFriendshipToFriendshipDTO).toList();
    }

    @Override
    public List<FriendshipDTO> getReceivedFriendRequests(String userId) {
        log.info("Getting received friend requests of {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        List<PendingFriendship> receivedFriendRequests = this.pendingFriendshipRepository.findByReceiver(user);
        return receivedFriendRequests.stream().map(friendshipMapper::pendingReceivedFriendshipToFriendshipDTO).toList();
    }

    private void deletePendingFriendship(String senderId, String receiverId) {
        log.info("Deleting pending friendship between {} and {}", senderId, receiverId);
        User sender = userRepository.findById(senderId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        PendingFriendship pendingFriendship = pendingFriendshipRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        pendingFriendshipRepository.delete(pendingFriendship);
    }
}

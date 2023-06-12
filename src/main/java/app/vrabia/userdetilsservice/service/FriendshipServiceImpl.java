package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.kafka.UserDTO;
import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;
import app.vrabia.userdetilsservice.dto.response.FriendshipStatus;
import app.vrabia.userdetilsservice.dto.response.PagedFriendshipsResponseDTO;
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
import app.vrabia.vrcommon.models.security.PublicEndpoints;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    private static final Integer DEFAULT_PAGE_SIZE = 6;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;

    @Override
    public void acceptFriendship(String senderId, String receiverId) {
        log.info("Accepting friendship between {} and {}", senderId, receiverId);
        userRepository.findById(senderId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        userRepository.findById(receiverId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        PendingFriendship pendingFriendship = pendingFriendshipRepository.findBySender_IdAndReceiver_Id(senderId, receiverId)
                .orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        Friendship friendship = friendshipMapper.pendingFriendshipToFriendship(pendingFriendship);
        LocalDate now = LocalDate.now();
        friendship.setFriendsSince(now);
        Friendship reverseFriendship = friendshipMapper.pendingFriendshipToReverseFriendship(pendingFriendship);
        reverseFriendship.setFriendsSince(now);

        friendshipRepository.save(friendship);
        friendshipRepository.save(reverseFriendship);
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
        Optional<Friendship> reverseFriendship = friendshipRepository.findByUser1AndUser2(friend, user);
        friendship.ifPresent(friendshipRepository::delete);
        reverseFriendship.ifPresent(friendshipRepository::delete);
    }

    @Override
    public PagedFriendshipsResponseDTO getFriends(String userId, String searchName, Integer page, Integer size) {
        log.info("Getting friends of {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        Pageable pageable = buildPageRequest(page, size);
        String search = Optional.ofNullable(searchName).orElse("");

        Page<Friendship> friendships = this.friendshipRepository.findByUser1_IdAndUser2_NameContainingIgnoreCase(userId, search, pageable);
        List<FriendshipDTO> friendshipDTOS = friendships.getContent().stream().map(friendship -> {
            FriendshipDTO friendshipDTO = friendshipMapper.friendshipToFriendshipDTO(friendship);
            User friend = friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1();
            friendshipDTO.setFriend(userMapper.userToUserDTO(friend));
            return friendshipDTO;
        }).toList();

        return new PagedFriendshipsResponseDTO(friendships.getTotalPages(), friendships.getNumber(), friendshipDTOS);
    }

    @Override
    public PagedFriendshipsResponseDTO getSentFriendRequests(String userId, String searchName, Integer page, Integer size) {
        log.info("Getting sent friend requests of {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        Pageable pageable = buildPageRequest(page, size);
        String search = Optional.ofNullable(searchName).orElse("");
        Page<PendingFriendship> sentFriendRequests = this.pendingFriendshipRepository.findBySender_IdAndReceiver_NameContainingIgnoreCase(userId, search, pageable);
        List<FriendshipDTO> friendshipDTOS = sentFriendRequests.getContent().stream().map(friendshipMapper::pendingSentFriendshipToFriendshipDTO).toList();
        return new PagedFriendshipsResponseDTO(sentFriendRequests.getTotalPages(), sentFriendRequests.getNumber(), friendshipDTOS);
    }

    @Override
    public PagedFriendshipsResponseDTO getReceivedFriendRequests(String userId, String searchName, Integer page, Integer size) {
        log.info("Getting received friend requests of {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        Pageable pageable = buildPageRequest(page, size);
        String search = Optional.ofNullable(searchName).orElse("");
        Page<PendingFriendship> receivedFriendRequests = this.pendingFriendshipRepository.findByReceiver_IdAndSender_NameContainingIgnoreCase(userId, search, pageable);
        List<FriendshipDTO> friendshipDTOS = receivedFriendRequests.getContent().stream().map(friendshipMapper::pendingReceivedFriendshipToFriendshipDTO).toList();
        return new PagedFriendshipsResponseDTO(receivedFriendRequests.getTotalPages(), receivedFriendRequests.getNumber(), friendshipDTOS);
    }

    @Override
    public PagedFriendshipsResponseDTO getUsersWithFriendshipStatus(String userId, String search, Integer page, Integer pageSize) {
        Pageable pageRequest = buildPageRequest(page, pageSize);
        String searchName = Optional.ofNullable(search).orElse("");
        Page<User> users = userRepository.findByNameContainingIgnoreCase(searchName, pageRequest);
        List<UserDTO> userDTOS = userMapper.usersToUsersDTO(users.getContent());
        List<FriendshipDTO> friendshipDTOS = userDTOS.stream().map(userDTO -> {
            Friendship friendship = friendshipRepository.findByUser1_IdAndUser2_Id(userId, userDTO.getId()).orElse(null);
            FriendshipDTO friendshipDTO = new FriendshipDTO();
            friendshipDTO.setFriend(userDTO);

            if (Objects.equals(userDTO.getId(), userId)) {
                friendshipDTO.setStatus(FriendshipStatus.SELF);
                return friendshipDTO;
            }

            if (friendship != null) {
                friendshipDTO.setStatus(FriendshipStatus.ACCEPTED);
                friendshipDTO.setFriendsSince(friendship.getFriendsSince());
                return friendshipDTO;
            }
            PendingFriendship pendingFriendship = pendingFriendshipRepository.findBySender_IdAndReceiver_Id(userId, userDTO.getId()).orElse(null);
            if (pendingFriendship != null) {
                friendshipDTO.setStatus(FriendshipStatus.SENT);
                return friendshipDTO;
            }

            pendingFriendship = pendingFriendshipRepository.findBySender_IdAndReceiver_Id(userDTO.getId(), userId).orElse(null);
            if (pendingFriendship != null) {
                friendshipDTO.setStatus(FriendshipStatus.RECEIVED);
                return friendshipDTO;
            }

            friendshipDTO.setStatus(FriendshipStatus.NONE);
            return friendshipDTO;
        }).toList();

        return new PagedFriendshipsResponseDTO(users.getTotalPages(), users.getNumber(), friendshipDTOS);
    }

    @Override
    public void removeRandomFriendships() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            List<Friendship> friendships = friendshipRepository.findByUser1_Id(user.getId());
            // remove 1/3 of the friendships
            int friendshipsToRemove = friendships.size() / 3;
            for (int i = 0; i < friendshipsToRemove; i++) {
                Friendship friendship = friendships.get(i);
                // find the reverse friendship and delete it
                Friendship reverseFriendship = friendshipRepository.findByUser1_IdAndUser2_Id(friendship.getUser2().getId(), friendship.getUser1().getId()).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
                friendshipRepository.delete(friendship);
                log.info("Deleted friendship between {} and {}", friendship.getUser1().getId(), friendship.getUser2().getId());
                friendshipRepository.delete(reverseFriendship);
                log.info("Deleted friendship between {} and {}", reverseFriendship.getUser1().getId(), reverseFriendship.getUser2().getId());
            }
        });
    }

    private void deletePendingFriendship(String senderId, String receiverId) {
        log.info("Deleting pending friendship between {} and {}", senderId, receiverId);
        userRepository.findById(senderId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));
        userRepository.findById(receiverId).orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        PendingFriendship pendingFriendship = pendingFriendshipRepository.findBySender_IdAndReceiver_Id(senderId, receiverId)
                .orElseThrow(() -> new VrabiaException(ErrorCodes.BAD_REQUEST));

        pendingFriendshipRepository.delete(pendingFriendship);
    }

    private Pageable buildPageRequest(Integer page, Integer pageSize) {
        Integer actualPage = page == null ? DEFAULT_PAGE_NUMBER : page;
        Integer actualPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        return PageRequest.of(actualPage, actualPageSize);
    }

    @Bean
    @Primary
    public PublicEndpoints publicEndpoints() {
        PublicEndpoints publicEndpoints = new PublicEndpoints();
        publicEndpoints.getEndpoints().add("/random-friend");
        return publicEndpoints;
    }
}

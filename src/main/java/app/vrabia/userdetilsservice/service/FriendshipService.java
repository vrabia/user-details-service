package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;

import java.util.List;

public interface FriendshipService {
    void acceptFriendship(String userId, String friendId);

    void rejectOrCancelFriendship(String userId, String friendId);

    void sendFriendshipRequest(String userId, String friendId);

    void removeFriendship(String userId, String friendId);

    List<FriendshipDTO> getFriends(String userId);

    List<FriendshipDTO> getSentFriendRequests(String userId);

    List<FriendshipDTO> getReceivedFriendRequests(String userId);
}

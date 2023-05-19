package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;

import java.util.List;

public interface FriendshipService {
    void acceptFriendship(String userId, String friendId);

    void rejectOrCancelFriendship(String userId, String friendId);

    void sendFriendshipRequest(String userId, String friendId);

    void removeFriendship(String userId, String friendId);

    List<FriendshipDTO> getFriends(String userId, String searchName, Integer page, Integer size);

    List<FriendshipDTO> getSentFriendRequests(String userId, String searchName, Integer page, Integer size);

    List<FriendshipDTO> getReceivedFriendRequests(String userId, String searchName, Integer page, Integer size);

    List<FriendshipDTO> getUsersWithFriendshipStatus(String userId, String search, Integer page, Integer pageSize);

}

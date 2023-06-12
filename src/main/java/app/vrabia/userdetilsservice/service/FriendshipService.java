package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.response.PagedFriendshipsResponseDTO;

public interface FriendshipService {
    void acceptFriendship(String userId, String friendId);

    void rejectOrCancelFriendship(String userId, String friendId);

    void sendFriendshipRequest(String userId, String friendId);

    void removeFriendship(String userId, String friendId);

    PagedFriendshipsResponseDTO getFriends(String userId, String searchName, Integer page, Integer size);

    PagedFriendshipsResponseDTO getSentFriendRequests(String userId, String searchName, Integer page, Integer size);

    PagedFriendshipsResponseDTO getReceivedFriendRequests(String userId, String searchName, Integer page, Integer size);

    PagedFriendshipsResponseDTO getUsersWithFriendshipStatus(String userId, String search, Integer page, Integer pageSize);

    void removeRandomFriendships();
}

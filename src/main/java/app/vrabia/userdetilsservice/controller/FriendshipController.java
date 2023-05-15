package app.vrabia.userdetilsservice.controller;

import app.vrabia.userdetilsservice.dto.request.FriendRequestDTO;
import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;
import app.vrabia.userdetilsservice.service.FriendshipService;
import app.vrabia.vrcommon.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JWTService jwtService;


    @PostMapping("/friend-request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendRequestDTO friend, @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Friend request received");
        String userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        friendshipService.sendFriendshipRequest(userId, friend.getFriendId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/friend-request")
    public ResponseEntity<Void> acceptFriendRequest(@RequestBody FriendRequestDTO friend, @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Friend request accepted");
        String userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        friendshipService.acceptFriendship(friend.getFriendId(), userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friend-request")
    public ResponseEntity<Void> rejectOrCancelFriendRequest(@RequestBody FriendRequestDTO friend, @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Friend request rejected");
        String userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        friendshipService.rejectOrCancelFriendship(userId, friend.getFriendId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/friend-request/sent")
    public ResponseEntity<List<FriendshipDTO>> getSentFriendRequests(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Get sent friend requests");
        String userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        return ResponseEntity.ok(friendshipService.getSentFriendRequests(userId));
    }

    @GetMapping("/friend-request/received")
    public ResponseEntity<List<FriendshipDTO>> getReceivedFriendRequests(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Get received friend requests");
        String userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        return ResponseEntity.ok(friendshipService.getReceivedFriendRequests(userId));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendshipDTO>> getFriends(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Get friends");
        String userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        return ResponseEntity.ok(friendshipService.getFriends(userId));
    }

    @DeleteMapping("/friends")
    public ResponseEntity<Void> removeFriend(@RequestBody FriendRequestDTO friend, @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Remove friend");
        String userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        friendshipService.removeFriendship(userId, friend.getFriendId());
        return ResponseEntity.noContent().build();
    }

    String getUserIdFromAuthorizationHeader(String authorizationHeader) {
        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        return jwtService.decodeJWT(token).getClaim("userId").asString();
    }
}

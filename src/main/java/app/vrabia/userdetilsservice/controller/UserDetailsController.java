package app.vrabia.userdetilsservice.controller;

import app.vrabia.userdetilsservice.dto.request.UserDetailsListRequestDTO;
import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;
import app.vrabia.userdetilsservice.dto.response.UserDetailsResponseDTO;
import app.vrabia.userdetilsservice.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserDetailsController {
    private final UserDetailsService userDetailsService;

    @GetMapping("/user-details/{userId}")
    public ResponseEntity<UserDetailsResponseDTO> getUserDetails(@PathVariable String userId) {
        log.info("Get user details");
        UserDetailsResponseDTO userDetailsResponseDTO = userDetailsService.getUserDetails(userId);
        return ResponseEntity.ok(userDetailsResponseDTO);
    }

    @PostMapping("/user-details")
    public ResponseEntity<List<UserDetailsResponseDTO>> getUserDetails(@RequestBody UserDetailsListRequestDTO userDetailsListRequestDTO) {
        log.info("Get user details");
        List<UserDetailsResponseDTO> userDetailsResponseDTO = userDetailsService.getUsersDetails(userDetailsListRequestDTO.getIds());
        return ResponseEntity.ok(userDetailsResponseDTO);
    }
}

package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.response.UserDetailsResponseDTO;

import java.util.List;

public interface UserDetailsService {
    UserDetailsResponseDTO getUserDetails(String userId);
    List<UserDetailsResponseDTO> getUsersDetails(List<String> userIds);
}

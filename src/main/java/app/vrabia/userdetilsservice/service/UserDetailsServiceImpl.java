package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.response.UserDetailsResponseDTO;
import app.vrabia.userdetilsservice.mappers.UserMapper;
import app.vrabia.userdetilsservice.model.User;
import app.vrabia.userdetilsservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDetailsResponseDTO getUserDetails(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(userMapper::userToUserDetailsResponseDTO).orElse(null);
    }

    @Override
    public List<UserDetailsResponseDTO> getUsersDetails(List<String> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return userMapper.usersToUsersDetailsResponseDTO(users);
    }
}

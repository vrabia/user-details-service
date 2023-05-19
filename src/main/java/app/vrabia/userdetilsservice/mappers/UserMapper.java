package app.vrabia.userdetilsservice.mappers;

import app.vrabia.userdetilsservice.dto.kafka.AddressDTO;
import app.vrabia.userdetilsservice.dto.kafka.UserDTO;
import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;
import app.vrabia.userdetilsservice.dto.response.UserDetailsResponseDTO;
import app.vrabia.userdetilsservice.model.Address;
import app.vrabia.userdetilsservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UserMapper {

    User userDTOToUser(UserDTO userDTO);

    Address addressDTOToAddress(AddressDTO addressDTO);

    UserDTO userToUserDTO(User user);

    AddressDTO addressToAddressDTO(Address address);

    UserDetailsResponseDTO userToUserDetailsResponseDTO(User user);

    List<UserDetailsResponseDTO> usersToUsersDetailsResponseDTO(List<User> users);

    List<UserDTO> usersToUsersDTO(List<User> users);
}

package app.vrabia.userdetilsservice.service;

import app.vrabia.userdetilsservice.dto.kafka.UserDTO;
import app.vrabia.userdetilsservice.mappers.UserMapper;
import app.vrabia.userdetilsservice.model.Address;
import app.vrabia.userdetilsservice.model.User;
import app.vrabia.userdetilsservice.repository.AddressRepository;
import app.vrabia.userdetilsservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaConsumer {

    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private UserMapper userMapper;


    @KafkaListener(topics = "auth-topic", groupId = "user-group")
    public void listen(UserDTO message) {
        // ToDO - save user to database
        log.info("Received message: " + message);
        Address address = userMapper.addressDTOToAddress(message.getAddress());
        addressRepository.save(address);

        log.info("Address saved: " + address);

        User user = userMapper.userDTOToUser(message);
        user.setAddress(address);
        User createdUser = userRepository.save(user);
        address.setUser(createdUser);
        addressRepository.save(address);

        log.info("User saved: " + user);

        System.out.println("Received message: " + message);
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
}
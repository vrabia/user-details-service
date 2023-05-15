package app.vrabia.userdetilsservice.dto.response;

import app.vrabia.userdetilsservice.dto.kafka.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendshipDTO {
    private UserDTO friend;
    private FriendshipStatus status;
    private LocalDate friendsSince;
}

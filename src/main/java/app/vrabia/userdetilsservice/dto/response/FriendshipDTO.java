package app.vrabia.userdetilsservice.dto.response;

import app.vrabia.userdetilsservice.dto.kafka.UserDTO;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FriendshipDTO {
    private UserDTO friend;
    private FriendshipStatus status;
    private LocalDate friendsSince;
}

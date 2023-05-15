package app.vrabia.userdetilsservice.dto.kafka;

import app.vrabia.userdetilsservice.model.MusicGenre;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String name;
    private AddressDTO address;
    private LocalDate birthdate;
    private String about;
    private MusicGenre genre;
}

package app.vrabia.userdetilsservice.dto.response;

import app.vrabia.userdetilsservice.model.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetailsResponseDTO {
    private String id;
    private String name;
    private LocalDate birthdate;
    private String about;
    private MusicGenre genre;
}

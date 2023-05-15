package app.vrabia.userdetilsservice.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddressDTO implements Serializable {
    private String country;
    private String city;
    private String zip;
}

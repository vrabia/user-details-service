package app.vrabia.userdetilsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "VRADDRESSES")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "AddressIdGenerator")
    @GenericGenerator(
            name = "AddressIdGenerator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "CITY")
    private String city;

    @Column(name = "ZIP")
    private String zip;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
}

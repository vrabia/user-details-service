package app.vrabia.userdetilsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "VRFRIENDSHIPS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "FriendshipIdGenerator")
    @GenericGenerator(
            name = "FriendshipIdGenerator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER1")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER2")
    private User user2;

    @Column(name = "FRIENDS_SINCE")
    private LocalDate friendsSince;
}

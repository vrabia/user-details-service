package app.vrabia.userdetilsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "VRPENDING_FRIENDSHIPS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingFriendship {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "PFriendshipIdGenerator")
    @GenericGenerator(
            name = "PFriendshipIdGenerator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER")
    private User receiver;
}

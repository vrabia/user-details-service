package app.vrabia.userdetilsservice.repository;

import app.vrabia.userdetilsservice.model.PendingFriendship;
import app.vrabia.userdetilsservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PendingFriendshipRepository extends JpaRepository<PendingFriendship, String> {
    List<PendingFriendship> findBySender(User sender);
    List<PendingFriendship> findByReceiver(User receiver);

    Optional<PendingFriendship> findBySenderAndReceiver(User sender, User receiver);
}

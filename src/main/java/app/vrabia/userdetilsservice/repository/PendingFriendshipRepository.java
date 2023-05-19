package app.vrabia.userdetilsservice.repository;

import app.vrabia.userdetilsservice.model.PendingFriendship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PendingFriendshipRepository extends JpaRepository<PendingFriendship, String> {
    List<PendingFriendship> findBySender_IdAndReceiver_NameContainingIgnoreCase(String sender, String name, Pageable pageable);
    List<PendingFriendship> findByReceiver_IdAndSender_NameContainingIgnoreCase(String receiver, String name, Pageable pageable);

    Optional<PendingFriendship> findBySender_IdAndReceiver_Id(String sender, String  receiver);
}

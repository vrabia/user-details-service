package app.vrabia.userdetilsservice.repository;

import app.vrabia.userdetilsservice.model.PendingFriendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingFriendshipRepository extends JpaRepository<PendingFriendship, String> {
    Page<PendingFriendship> findBySender_IdAndReceiver_NameContainingIgnoreCase(String sender, String name, Pageable pageable);
    Page<PendingFriendship> findByReceiver_IdAndSender_NameContainingIgnoreCase(String receiver, String name, Pageable pageable);

    Optional<PendingFriendship> findBySender_IdAndReceiver_Id(String sender, String  receiver);
}

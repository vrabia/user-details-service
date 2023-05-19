package app.vrabia.userdetilsservice.repository;

import app.vrabia.userdetilsservice.model.Friendship;
import app.vrabia.userdetilsservice.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface FriendshipRepository extends JpaRepository<Friendship, String> {
    List<Friendship> findByUser1(User userId);
    List<Friendship> findByUser2(User userId);

    List<Friendship> findByUser1_IdAndUser2_NameContainingIgnoreCase(String user, String name2, Pageable pageable);

    Optional<Friendship> findByUser1AndUser2(User user1, User user2);

    Optional<Friendship> findByUser1_IdAndUser2_Id(String userId, String id);
}

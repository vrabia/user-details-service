package app.vrabia.userdetilsservice.repository;

import app.vrabia.userdetilsservice.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

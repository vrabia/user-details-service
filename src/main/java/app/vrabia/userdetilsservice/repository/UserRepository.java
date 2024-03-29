package app.vrabia.userdetilsservice.repository;

import app.vrabia.userdetilsservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

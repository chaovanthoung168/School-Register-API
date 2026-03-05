package co.thoung.schoolregistration.feature.user;

import co.thoung.schoolregistration.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(UUID userId);
    Boolean existsByUsername(String username);
    Boolean existsByEmail (String email);
    Optional<User> findByEmail (String email);

}

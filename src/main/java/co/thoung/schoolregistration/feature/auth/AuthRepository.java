package co.thoung.schoolregistration.feature.auth;

import co.thoung.schoolregistration.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}

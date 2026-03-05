package co.thoung.schoolregistration.feature.auth;

import co.thoung.schoolregistration.domain.EmailVerification;
import co.thoung.schoolregistration.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findByUser(User user);
}

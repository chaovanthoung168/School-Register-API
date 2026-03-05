package co.thoung.schoolregistration.feature.RegistrationTransaction;

import co.thoung.schoolregistration.domain.RegistrationTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationTransactionRepository extends JpaRepository<RegistrationTransaction, Integer> {
}

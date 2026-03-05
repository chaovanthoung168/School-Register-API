package co.thoung.schoolregistration.feature.address;

import co.thoung.schoolregistration.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

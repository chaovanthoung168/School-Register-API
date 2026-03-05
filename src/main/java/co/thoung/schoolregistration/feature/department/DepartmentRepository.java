package co.thoung.schoolregistration.feature.department;

import co.thoung.schoolregistration.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);
    Boolean existsByCode(String code);
    Boolean existsByName(String name);

}

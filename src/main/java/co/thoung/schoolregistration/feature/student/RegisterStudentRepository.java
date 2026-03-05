package co.thoung.schoolregistration.feature.student;

import co.thoung.schoolregistration.domain.RegisterStudent;
import co.thoung.schoolregistration.feature.student.dto.RegisterStudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterStudentRepository extends JpaRepository<RegisterStudent, Long> {

    Optional<RegisterStudent> findByStudentNo(String studentNo);

}

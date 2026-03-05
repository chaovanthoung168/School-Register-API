package co.thoung.schoolregistration.feature.Payment;

import co.thoung.schoolregistration.domain.PaymentManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentManagementRepository extends JpaRepository<PaymentManagement,Integer> {

//    @Query("SELECT p FROM PaymentManagement p WHERE p.registrationTransaction.registerStudent.user.id = :studentId")
//    List<PaymentManagement> findByStudentId(@Param("studentId") UUID studentId);

    // find by academic year
    List<PaymentManagement> findByRegistrationTransaction_AcademicYear(String academicYear);

    @Query("SELECT p FROM PaymentManagement p WHERE p.registrationTransaction.registerStudent.user.userId = :userId")
    List<PaymentManagement> findByUserId(@Param("userId") UUID userId);

    List<PaymentManagement> findByRegistrationTransaction_RegisterStudent_Id(Long studentId);
}

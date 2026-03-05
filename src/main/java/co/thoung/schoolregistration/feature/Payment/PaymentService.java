package co.thoung.schoolregistration.feature.Payment;


import co.thoung.schoolregistration.domain.PaymentManagement;
import co.thoung.schoolregistration.feature.Payment.dto.PaymentResponse;
import co.thoung.schoolregistration.feature.Payment.dto.QrSession;
import co.thoung.schoolregistration.feature.RegistrationTransaction.RegistrationTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;


public interface PaymentService {

    void savePayment(QrSession qrSession);

    List<PaymentResponse> findAllPayments();

    PaymentResponse findPaymentById(Integer id);

    List<PaymentResponse> getByStudent(Long studentId);

    List<PaymentResponse> getByAcademicYear(String academicYear);

    List<PaymentResponse> getByUserId(UUID userId);

}

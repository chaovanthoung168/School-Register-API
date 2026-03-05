package co.thoung.schoolregistration.feature.Payment;

import co.thoung.schoolregistration.domain.PaymentManagement;
import co.thoung.schoolregistration.domain.PaymentType;
import co.thoung.schoolregistration.domain.RegistrationTransaction;
import co.thoung.schoolregistration.feature.Payment.dto.PaymentResponse;
import co.thoung.schoolregistration.feature.Payment.dto.QrSession;
import co.thoung.schoolregistration.feature.RegistrationTransaction.RegistrationTransactionRepository;
import co.thoung.schoolregistration.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentManagementRepository paymentManagementRepository;
    private final RegistrationTransactionRepository registrationTransactionRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public void savePayment(QrSession session) {
        try {
            RegistrationTransaction registration = registrationTransactionRepository
                    .findById(session.registrationId())
                    .orElseThrow(() -> new RuntimeException("Registration not found: " + session.registrationId()));

            PaymentManagement payment = new PaymentManagement();
            payment.setPaymentRef(session.md5());
            payment.setAmount(session.amount());
            payment.setPaymentMethod("KHQR");
            payment.setPaymentType(session.paymentType().name());
            payment.setCreatedAt(LocalDate.now());
            payment.setRegistrationTransaction(registration);

            paymentManagementRepository.save(payment);

            // only mark fully paid if FULL payment
            if (session.paymentType() == PaymentType.FULL) {
                registration.setStatus(true);
                registrationTransactionRepository.save(registration);
            }

            System.out.println("✅ Payment saved — type: " + session.paymentType() + ", amount: " + session.amount());
        } catch (Exception e) {
            System.err.println("❌ Failed to save payment: " + e.getMessage());
        }
    }

    @Override
    public List<PaymentResponse> findAllPayments() {
        List<PaymentManagement> paymentManagements = paymentManagementRepository.findAll();

        if (paymentManagements.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return paymentManagements.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Override
    public PaymentResponse findPaymentById(Integer id) {

        PaymentManagement paymentManagement = paymentManagementRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Payment not found: " + id)
        );
        return paymentMapper.toPaymentResponse(paymentManagement);
    }

    // service
    @Override
    public List<PaymentResponse> getByStudent(Long studentId) {
        List<PaymentManagement> payments = paymentManagementRepository
                .findByRegistrationTransaction_RegisterStudent_Id(studentId);

        if (payments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No payments found for student: " + studentId);
        }

        return payments.stream().map(paymentMapper::toPaymentResponse).toList();
    }

    @Override
    public List<PaymentResponse> getByAcademicYear(String academicYear) {
        List<PaymentManagement> payments = paymentManagementRepository
                .findByRegistrationTransaction_AcademicYear(academicYear);

        if (payments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No payments found for academic year: " + academicYear);
        }

        return payments.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getByUserId(UUID userId) {
        List<PaymentManagement> payments = paymentManagementRepository
                .findByUserId(userId);

        if (payments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No payments found for user: " + userId);
        }

        return payments.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }
}

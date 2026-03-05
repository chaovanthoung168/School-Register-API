package co.thoung.schoolregistration.feature.Payment;

import co.thoung.schoolregistration.feature.Payment.dto.BakongRequest;
import co.thoung.schoolregistration.feature.Payment.dto.PaymentResponse;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final KhqrService khqrService;

    @PostMapping("/generate-qr")
    public KHQRResponse<KHQRData> generateQR(@RequestBody BakongRequest request){
        return khqrService.generateQR(request);
    }
    @GetMapping("/verify/{md5}")
    public Map<String, Object> verifyPayment(@PathVariable String md5) {
        return khqrService.checkTransaction(md5).block();
    }

    @GetMapping
    List<PaymentResponse> findAll (){
        return paymentService.findAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentResponse findById(@PathVariable Integer id){
        return paymentService.findPaymentById(id);
    }

    @GetMapping("/student/{id}")
    public List<PaymentResponse> findByStudentId(@PathVariable Long id){
        return paymentService.getByStudent(id);
    }

    @GetMapping("/user/{userId}")
    public List<PaymentResponse> getByUserId(@PathVariable UUID userId) {
        return paymentService.getByUserId(userId);
    }

    @GetMapping("/academic-year/{academicYear}")
    public List<PaymentResponse> getByAcademicYear(@PathVariable String academicYear) {
        return paymentService.getByAcademicYear(academicYear);
    }

}

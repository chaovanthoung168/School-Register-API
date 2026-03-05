package co.thoung.schoolregistration.feature.Payment.dto;


import java.time.LocalDate;

public record PaymentResponse(
        Integer id,
        String paymentRef,
        Double amount,
        String paymentMethod,
        String paymentType,
        LocalDate createdAt
) {
}

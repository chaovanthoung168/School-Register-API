package co.thoung.schoolregistration.feature.Payment.dto;

//public record BakongRequest(
//        double amount
//) {
//}

import co.thoung.schoolregistration.domain.PaymentType;

public record BakongRequest(
        Integer registrationId,
        PaymentType paymentType  // ✅ FULL or SEMESTER
) {}

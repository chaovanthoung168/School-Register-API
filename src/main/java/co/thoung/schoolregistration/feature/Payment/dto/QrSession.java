package co.thoung.schoolregistration.feature.Payment.dto;

import co.thoung.schoolregistration.domain.PaymentType;

import java.time.Instant;

//public record QrSession(
//        String md5,
//        Instant expiresAt) {
//    public boolean isExpired() {
//        return Instant.now().isAfter(expiresAt);
//    }
//}

public record QrSession(
        String md5,
        Instant expiresAt,
        Integer registrationId,
        Double amount,
        PaymentType paymentType
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
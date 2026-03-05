package co.thoung.schoolregistration.feature.Payment.dto;

import lombok.Data;

@Data
public class QRGenerateResponse {
    private String qrString;   // the raw KHQR string to encode into QR image
    private String md5;        // md5 hash used to check payment status
    private Double amount;
    private String currency;
}
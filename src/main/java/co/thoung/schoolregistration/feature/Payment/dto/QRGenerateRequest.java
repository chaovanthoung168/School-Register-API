package co.thoung.schoolregistration.feature.Payment.dto;

import lombok.Data;

@Data
public class QRGenerateRequest {
    private Integer registrationId;
    private Double amount;
    private String currency;
}

package co.thoung.schoolregistration.feature.Payment.dto;

import lombok.Data;


@Data
public class BakongCheckResponse {
    private Integer responseCode;   // 0 = paid, non-zero = not yet
    private String responseMessage;
    private Object data;
}

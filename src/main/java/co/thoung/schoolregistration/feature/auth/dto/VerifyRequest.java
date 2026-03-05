package co.thoung.schoolregistration.feature.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyRequest(
        @NotBlank(message = "Email must be not blank")
        String email,
        @NotBlank(message = "verification must be not blank")
        String verificationCode
) {
}

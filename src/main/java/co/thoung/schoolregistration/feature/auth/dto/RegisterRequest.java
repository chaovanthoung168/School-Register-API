package co.thoung.schoolregistration.feature.auth.dto;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String confirmPassword
) {
}

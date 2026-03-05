package co.thoung.schoolregistration.feature.user.dto;

public record UpdateUserRequest(
        String username,
        String email
) {
}

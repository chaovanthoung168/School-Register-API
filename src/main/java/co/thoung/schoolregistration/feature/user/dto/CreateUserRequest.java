package co.thoung.schoolregistration.feature.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserRequest(
   String username,
   String email,
   String password,
   String confirmPassword
) {
}

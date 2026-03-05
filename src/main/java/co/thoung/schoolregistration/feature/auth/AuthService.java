package co.thoung.schoolregistration.feature.auth;

import co.thoung.schoolregistration.feature.auth.dto.*;
import co.thoung.schoolregistration.feature.user.dto.UserResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {
    JwtResponse login(@Valid LoginRequest loginRequest);

    JwtResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest);

    UserResponse registerUser(RegisterRequest registerRequest) throws MessagingException;

    void verify(@Valid VerifyRequest verifyRequest);

    UserResponse verifyMe(Jwt jwt);
}

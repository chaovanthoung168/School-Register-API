package co.thoung.schoolregistration.feature.auth;


import co.thoung.schoolregistration.feature.auth.dto.*;
import co.thoung.schoolregistration.feature.user.dto.UserResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    JwtResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    JwtResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    UserResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/verify")
    void verify(@Valid @RequestBody VerifyRequest verifyRequest){
        authService.verify(verifyRequest);
    }


    @GetMapping("/me")
    UserResponse verifyMe (@AuthenticationPrincipal Jwt jwt){
        return authService.verifyMe(jwt);
    }
}

package co.thoung.schoolregistration.feature.auth;


import co.thoung.schoolregistration.domain.EmailVerification;
import co.thoung.schoolregistration.domain.Role;
import co.thoung.schoolregistration.domain.User;
import co.thoung.schoolregistration.feature.auth.dto.*;
import co.thoung.schoolregistration.feature.user.UserRepository;
import co.thoung.schoolregistration.feature.user.dto.UserResponse;
import co.thoung.schoolregistration.mapper.UserMapper;
import co.thoung.schoolregistration.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final AuthRepository authRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final String TOKEN_TYPE = "Bearer";



    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    @Value("${spring.mail.username}")
    private String adminEmail;

    private JwtEncoder jwtEncoderRefreshToken;
    private JwtDecoder jwtDecoder;

    @Autowired
    @Qualifier("jwtEncoderRefreshToken")
    public void setJwtEncoderRefreshToken(JwtEncoder jwtEncoderRefreshToken) {
        this.jwtEncoderRefreshToken = jwtEncoderRefreshToken;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {



        // Authentication client with username and password
        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        auth = daoAuthenticationProvider.authenticate(auth);
        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        String userEmail = auth.getName();

        // Create access token claim set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs", "reactjs"))
                .subject(userEmail)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .claim("email",userEmail)
                .build();

        // Create access token claim set
        JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nestjs", "reactjs"))
                .subject(userEmail)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .claim("email",userEmail)
                .build();

        // AccessToken
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);

        // RefreshToken
        JwtEncoderParameters jwtEncoderParameterRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
        Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParameterRefreshToken);

        String accessToken = jwt.getTokenValue();
        String refreshToken = jwtRefreshToken.getTokenValue();

        return JwtResponse.builder()
                .tokenType(TOKEN_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenRequest.token());
        auth = jwtAuthenticationProvider.authenticate(auth);

        String scops = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        Instant now = Instant.now();
        Jwt jwt = (Jwt) auth.getPrincipal();

        String userEmail = jwt.getClaimAsString("email");
        if (userEmail == null) {
            userEmail = jwt.getId(); // fallback to ID if email claim doesn't exist
        }

        // Create access token claim set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nesxtjs", "reactjs"))
                .subject(userEmail)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .claim("scope", scops)
                .claim("email",userEmail)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt encodedJwt = jwtEncoder.encode(jwtEncoderParameters);

        String accessToken = encodedJwt.getTokenValue();
        String refreshToken = refreshTokenRequest.token();

        if (Duration.between(Instant.now(), jwt.getExpiresAt()).toDays() < 2) {
            JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                    .id(auth.getName())
                    .issuedAt(now)
                    .issuer("web")
                    .audience(List.of("nestjs", "reactjs"))
                    .subject(userEmail)
                    .expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .claim("email",userEmail)
                    .build();
            JwtEncoderParameters jwtEncoderParameterRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
            Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParameterRefreshToken);
            refreshToken = jwtRefreshToken.getTokenValue();
        }
        return JwtResponse.builder()
                .tokenType(TOKEN_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public UserResponse registerUser(RegisterRequest registerRequest) throws MessagingException {

        if (authRepository.existsByUsername(registerRequest.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }
        if (authRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }
        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Passwords do not match");
        }

        User user = userMapper.fromUserRegister(registerRequest);
        user.setUserId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setIsDeleted(false);
        user.setIsVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role USER not found")));
        user.setRoles(roles);

        User saveUser = authRepository.save(user);

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setVerificationCode(RandomUtil.random6Digits());
        emailVerification.setExpiryTime(LocalTime.now().plusMinutes(5));
        emailVerification.setUser(user);
        emailVerificationRepository.save(emailVerification);

        // ✅ use concatenation to avoid String.format() % escaping issues
        String code = emailVerification.getVerificationCode();
        String html = "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>School Registration - Email Verification</title>"
                + "</head>"
                + "<body style='margin:0;padding:0;font-family:Helvetica Neue,Arial,sans-serif;background-color:#f0ede8;'>"

                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0'>"
                + "<tr><td align='center' style='padding:40px 20px;'>"

                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0'"
                + " style='max-width:580px;background-color:#faf8f5;border-radius:12px;"
                + " box-shadow:0 4px 24px rgba(0,0,0,0.08);overflow:hidden;'>"

                // Top accent bar
                + "<tr><td style='height:5px;background:linear-gradient(90deg,#c8451a,#e8863a,#c8451a);'></td></tr>"

                // Header
                + "<tr><td style='padding:36px 40px 28px;border-bottom:1px solid #e8e4de;'>"
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>"
                + "<td>"
                + "<table role='presentation' cellpadding='0' cellspacing='0'><tr>"
                + "<td style='width:48px;height:48px;background-color:#f5ede9;"
                + "border:1px solid rgba(200,69,26,0.2);border-radius:10px;"
                + "text-align:center;vertical-align:middle;font-size:22px;'>&#127891;</td>"
                + "<td style='padding-left:14px;'>"
                + "<div style='font-size:10px;font-weight:600;letter-spacing:0.2em;"
                + "color:#c8451a;text-transform:uppercase;margin-bottom:3px;'>School Registration</div>"
                + "<div style='font-size:18px;font-weight:700;color:#1a1714;letter-spacing:-0.02em;'>Email Verification</div>"
                + "</td></tr></table>"
                + "</td>"
                + "<td align='right' style='vertical-align:top;'>"
                + "<span style='display:inline-block;background-color:#f5ede9;"
                + "border:1px solid rgba(200,69,26,0.25);color:#c8451a;font-size:10px;"
                + "font-weight:600;letter-spacing:0.1em;padding:4px 10px;"
                + "border-radius:20px;text-transform:uppercase;'>Secure Code</span>"
                + "</td>"
                + "</tr></table>"
                + "</td></tr>"

                // Body
                + "<tr><td style='padding:32px 40px;'>"

                // Greeting
                + "<p style='font-size:15px;color:#3a3530;line-height:1.7;margin:0 0 20px;'>"
                + "Thank you for registering with our school system. To complete your registration, "
                + "please verify your email address using the code below."
                + "</p>"

                // Code box
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0' style='margin:24px 0;'>"
                + "<tr><td style='background-color:#f5ede9;border:1px solid rgba(200,69,26,0.2);"
                + "border-radius:10px;padding:24px;text-align:center;'>"
                + "<div style='font-size:11px;font-weight:600;letter-spacing:0.2em;"
                + "color:#c8451a;text-transform:uppercase;margin-bottom:10px;'>Verification Code</div>"
                + "<div style='font-size:40px;font-weight:800;letter-spacing:10px;"
                + "color:#1a1714;font-family:Courier New,monospace;'>" + code + "</div>"
                + "</td></tr></table>"

                // Info grid
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0' style='margin:20px 0;'>"
                + "<tr>"
                + "<td width='50%' style='padding-right:6px;'>"
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>"
                + "<td style='background-color:#f0ede8;border:1px solid #ddd9d2;border-radius:8px;padding:12px 14px;'>"
                + "<div style='font-size:9px;font-weight:600;letter-spacing:0.15em;color:#8c8178;"
                + "text-transform:uppercase;margin-bottom:4px;'>Valid For</div>"
                + "<div style='font-size:14px;font-weight:600;color:#1a1714;'>5 Minutes</div>"
                + "</td></tr></table>"
                + "</td>"
                + "<td width='50%' style='padding-left:6px;'>"
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>"
                + "<td style='background-color:#f0ede8;border:1px solid #ddd9d2;border-radius:8px;padding:12px 14px;'>"
                + "<div style='font-size:9px;font-weight:600;letter-spacing:0.15em;color:#8c8178;"
                + "text-transform:uppercase;margin-bottom:4px;'>Single Use</div>"
                + "<div style='font-size:14px;font-weight:600;color:#1a1714;'>One Time Only</div>"
                + "</td></tr></table>"
                + "</td>"
                + "</tr></table>"

                // Warning box
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0' style='margin:20px 0;'>"
                + "<tr><td style='background-color:#fffbf0;border:1px solid #f5e6b0;border-radius:8px;padding:12px 16px;'>"
                + "<p style='font-size:13px;color:#7a6020;margin:0;line-height:1.6;'>"
                + "&#9888;&#65039; &nbsp;Do not share this code with anyone. "
                + "Our staff will never ask for your verification code."
                + "</p></td></tr></table>"

                // Divider
                + "<hr style='border:none;border-top:1px solid #e8e4de;margin:24px 0;'>"

                // Steps
                + "<p style='font-size:13px;font-weight:600;color:#1a1714;"
                + "text-transform:uppercase;letter-spacing:0.1em;margin:0 0 12px;'>How to verify</p>"
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0'>"

                + "<tr><td style='padding:6px 0;'>"
                + "<table role='presentation' cellpadding='0' cellspacing='0'><tr>"
                + "<td style='width:24px;height:24px;background-color:#c8451a;border-radius:50%;"
                + "text-align:center;vertical-align:middle;font-size:11px;font-weight:700;color:#fff;'>1</td>"
                + "<td style='padding-left:12px;font-size:13px;color:#555047;line-height:1.5;'>"
                + "Go back to the school registration portal</td>"
                + "</tr></table></td></tr>"

                + "<tr><td style='padding:6px 0;'>"
                + "<table role='presentation' cellpadding='0' cellspacing='0'><tr>"
                + "<td style='width:24px;height:24px;background-color:#c8451a;border-radius:50%;"
                + "text-align:center;vertical-align:middle;font-size:11px;font-weight:700;color:#fff;'>2</td>"
                + "<td style='padding-left:12px;font-size:13px;color:#555047;line-height:1.5;'>"
                + "Enter the 6-digit code in the verification field</td>"
                + "</tr></table></td></tr>"

                + "<tr><td style='padding:6px 0;'>"
                + "<table role='presentation' cellpadding='0' cellspacing='0'><tr>"
                + "<td style='width:24px;height:24px;background-color:#c8451a;border-radius:50%;"
                + "text-align:center;vertical-align:middle;font-size:11px;font-weight:700;color:#fff;'>3</td>"
                + "<td style='padding-left:12px;font-size:13px;color:#555047;line-height:1.5;'>"
                + "Your account will be activated instantly</td>"
                + "</tr></table></td></tr>"

                + "</table>"
                + "</td></tr>"

                // Footer
                + "<tr><td style='padding:20px 40px 28px;border-top:1px solid #e8e4de;background-color:#f5f3f0;'>"
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>"
                + "<td>"
                + "<p style='font-size:11px;color:#8c8178;margin:0 0 4px;line-height:1.6;'>"
                + "If you did not create an account, please ignore this email or contact us at "
                + "<a href='mailto:support@school.edu.kh' style='color:#c8451a;text-decoration:none;'>"
                + "support@school.edu.kh</a></p>"
                + "<p style='font-size:11px;color:#b0a89e;margin:0;'>"
                + "&copy; 2025 School Registration System &middot; Cambodia</p>"
                + "</td>"
                + "<td align='right' style='vertical-align:bottom;font-size:20px;'>&#127472;&#127469;</td>"
                + "</tr></table>"
                + "</td></tr>"

                + "</table>"
                + "</td></tr></table>"
                + "</body></html>";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setSubject("Email Verification - School Registration");
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setFrom(adminEmail);
        mimeMessageHelper.setText(html, true);
        javaMailSender.send(mimeMessage);

        return userMapper.userToUserDto(saveUser);
    }


    @Override
    public void verify(VerifyRequest verifyRequest) {
        User user = userRepository.findByEmail(verifyRequest.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found")
        );

        EmailVerification emailVerification = emailVerificationRepository
                .findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification not found"));

        if (!emailVerification.getVerificationCode().equals(verifyRequest.verificationCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code does not match");
        }

        if (LocalTime.now().isAfter(emailVerification.getExpiryTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has expired");
        }

        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public UserResponse verifyMe(Jwt jwt) {
        String username = jwt.getId(); // or jwt.getSubject()

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        return userMapper.userToUserDto(user);
    }


}

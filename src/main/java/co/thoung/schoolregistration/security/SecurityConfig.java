package co.thoung.schoolregistration.security;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt ->{
            String id = jwt.getId();
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(id);
            return userDetails.getAuthorities()
                    .stream()
                    .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
                    .collect(Collectors.toList());
        };
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("jwtDecoderRefreshToken")JwtDecoder jwtDecoderRefreshToken) {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoderRefreshToken);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return jwtAuthenticationProvider;
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    private static final String[] WHITE_LIST = {
            // ── Auth (public) ──
            "/api/v1/auth/**",

            // ── Swagger ──
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(r -> r
                .requestMatchers(WHITE_LIST).permitAll()

                .requestMatchers(HttpMethod.POST,"/api/v1/register-student").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.GET,"/api/v1/register-student").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,"/api/v1/register-student/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/v1/register-student/{id}/disable","/api/v1/register-student/{id}/disable").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/v1/departments").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.PATCH,"/api/v1/departments/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/v1/departments/{id}","/api/v1/departments/{id}/disable","/api/v1/departments/{id}/enable").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/Registration-Transaction").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.GET,"/api/v1/Registration-Transaction").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/v1/Registration-Transaction/{id}").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/Registration-Transaction/{id}/disable","/api/v1/Registration-Transaction/{id}/enable").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/generate-qr").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/payments/verify/{md5}").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/payments/{id}","/api/v1/payments/student/{id}","/api/v1/payments/user/{userId}").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.GET,"/api/v1/payments","/api/v1/payments/academic-year/{academicYear}").hasRole("ADMIN")
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        http.csrf(AbstractHttpConfigurer::disable);


        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}

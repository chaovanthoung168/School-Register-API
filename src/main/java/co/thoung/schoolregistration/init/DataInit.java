package co.thoung.schoolregistration.init;

import co.thoung.schoolregistration.domain.Role;
import co.thoung.schoolregistration.domain.User;
import co.thoung.schoolregistration.feature.auth.RoleRepository;
import co.thoung.schoolregistration.feature.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    void init() {
        initRoles();
        initDataUser();
    }

    private void initRoles(){
        if (roleRepository.count() > 0) {
            return;
        }
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("USER").build());
        roles.add(Role.builder().name("ADMIN").build());

        roleRepository.saveAll(roles);
    }

    private void initDataUser() {

        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }

        User userAdmin = new User();
        userAdmin.setUsername("admin");
        userAdmin.setEmail("admin@gmail.com");
        userAdmin.setPassword(passwordEncoder.encode("admin123"));
        userAdmin.setIsDeleted(false);
        userAdmin.setUserId(UUID.randomUUID());
        userAdmin.setCreatedAt(LocalDateTime.now());

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(2).orElseThrow());
        userAdmin.setRoles(roles);

        userRepository.save(userAdmin);
    }

}

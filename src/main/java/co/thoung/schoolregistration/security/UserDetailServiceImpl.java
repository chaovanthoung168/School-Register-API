package co.thoung.schoolregistration.security;

import co.thoung.schoolregistration.domain.User;
import co.thoung.schoolregistration.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        User User = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(User);
        return customUserDetails;
    }
}

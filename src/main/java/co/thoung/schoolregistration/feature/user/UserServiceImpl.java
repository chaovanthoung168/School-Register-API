package co.thoung.schoolregistration.feature.user;

import co.thoung.schoolregistration.domain.User;
import co.thoung.schoolregistration.feature.user.dto.CreateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UpdateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UserResponse;
import co.thoung.schoolregistration.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createNewUser(CreateUserRequest createUserRequest) {

        log.info("create user {}",createUserRequest);

        // validation username
        if (userRepository.existsByUsername(createUserRequest.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        // validation email
        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        // validation password
        if (!createUserRequest.password().equals(createUserRequest.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }
        User user = userMapper.createUserDtoToUser(createUserRequest);
        user.setUserId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setIsDeleted(false);
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " User is empty");
        }
        return
                users.stream()
                        .filter(user -> user.getIsDeleted() == false)
                        .map(userMapper::userToUserDto)
                        .toList();
    }

    @Override
    public UserResponse findByUuid(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserResponse updateUserByUuid(UUID uuid, UpdateUserRequest updateUserRequest) {

        // validation user id
        User user = userRepository.findByUserId(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        userMapper.fromUpdateUserRequestPartially(updateUserRequest,user);
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public UserResponse disableUserByUuid(UUID uuid) {

        // validation user id
        User user = userRepository.findByUserId(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        log.info(" user {}",user);
        user.setIsDeleted(true);
        log.info("disable user {}",user);
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public UserResponse enableUserByUuid(UUID uuid) {
        // validation user id
        User user = userRepository.findByUserId(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        user.setIsDeleted(false);
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }
}

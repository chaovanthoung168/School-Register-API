package co.thoung.schoolregistration.feature.user;

import co.thoung.schoolregistration.feature.user.dto.CreateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UpdateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createNewUser(CreateUserRequest createUserRequest);

    List<UserResponse> findAll();

    UserResponse findByUuid(UUID userId);

    UserResponse updateUserByUuid(UUID uuid, UpdateUserRequest updateUserRequest);

    UserResponse disableUserByUuid(UUID uuid);

    UserResponse enableUserByUuid(UUID uuid);
}

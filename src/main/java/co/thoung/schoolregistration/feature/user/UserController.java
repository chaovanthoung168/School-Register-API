package co.thoung.schoolregistration.feature.user;

import co.thoung.schoolregistration.domain.User;
import co.thoung.schoolregistration.feature.user.dto.CreateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UpdateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // create new user
    @PostMapping
    UserResponse createNewUser(@RequestBody CreateUserRequest createUserRequest){
        return userService.createNewUser(createUserRequest);
    }

    // get all user
    @GetMapping
    List<UserResponse> getAllUser(){
        return userService.findAll();
    }

    // find user by uuid
    @GetMapping("/{userId}")
    UserResponse findUserByUuid(@PathVariable UUID userId){
        return userService.findByUuid(userId);
    }

    // update by uudi
    @PutMapping("/{uuid}")
    UserResponse updateUser(@PathVariable UUID uuid, @RequestBody UpdateUserRequest updateUserRequest){
        return userService.updateUserByUuid(uuid, updateUserRequest);
    }

    // disable user
    @GetMapping("/{uuid}/disable")
    UserResponse disableUserByUuid(@PathVariable UUID uuid){
        return userService.disableUserByUuid(uuid);
    }

    // enable user
    @GetMapping("/{uuid}/enable")
    UserResponse enableUserByUuid(@PathVariable UUID uuid){
        return userService.enableUserByUuid(uuid);
    }
}

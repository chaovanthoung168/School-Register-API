package co.thoung.schoolregistration.mapper;

import co.thoung.schoolregistration.domain.User;
import co.thoung.schoolregistration.feature.auth.dto.RegisterRequest;
import co.thoung.schoolregistration.feature.user.dto.CreateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UpdateUserRequest;
import co.thoung.schoolregistration.feature.user.dto.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User createUserDtoToUser(CreateUserRequest createUserRequest);
    UserResponse userToUserDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateUserRequestPartially(UpdateUserRequest updateUserRequest,
                                        @MappingTarget User user);

    User fromUserRegister(RegisterRequest registerRequest);
}

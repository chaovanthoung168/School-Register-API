package co.thoung.schoolregistration.mapper;

import co.thoung.schoolregistration.domain.Address;
import co.thoung.schoolregistration.domain.RegisterStudent;
import co.thoung.schoolregistration.feature.address.dto.AddressResponse;
import co.thoung.schoolregistration.feature.student.dto.RegisterStudentResponse;
import co.thoung.schoolregistration.feature.student.dto.UpdateStudentRegisterRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RegisterStudentMapper {


    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "department", target = "departmentResponse")
    @Mapping(source = "address", target = "addressResponse")
    RegisterStudentResponse studentToStudentResponse(RegisterStudent registerStudent);

    AddressResponse toAddressResponse(Address address);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department", ignore = true) // ⭐ FIX
    @Mapping(target = "address", ignore = true)    // handled separately
    void updateStudentFromDto(
            UpdateStudentRegisterRequest updateStudentRegisterRequest,
            @MappingTarget RegisterStudent student
    );

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(
            UpdateStudentRegisterRequest updateStudentRegisterRequest,
            @MappingTarget Address address
    );
}

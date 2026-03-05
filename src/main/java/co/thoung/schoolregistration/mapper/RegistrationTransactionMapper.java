package co.thoung.schoolregistration.mapper;

import co.thoung.schoolregistration.domain.RegistrationTransaction;
import co.thoung.schoolregistration.feature.RegistrationTransaction.dto.RegistrationTransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationTransactionMapper {
    @Mapping(source = "registerStudent.id", target = "registerStudentId")
    RegistrationTransactionResponse toRegistrationResponse(RegistrationTransaction registrationTransaction);

}

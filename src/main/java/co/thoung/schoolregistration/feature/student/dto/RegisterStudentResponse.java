package co.thoung.schoolregistration.feature.student.dto;

import co.thoung.schoolregistration.domain.Gender;
import co.thoung.schoolregistration.feature.address.dto.AddressResponse;
import co.thoung.schoolregistration.feature.department.dto.DepartmentResponse;

import java.time.LocalDate;
import java.util.UUID;

public record RegisterStudentResponse(
        UUID userId,
        String studentNo,
        String nameKh,
        String nameEng,
        Gender gender,
        LocalDate dob,
        String phoneNumber,
        Integer yearOFStudy,
        String highSchool,
        AddressResponse addressResponse,
        DepartmentResponse departmentResponse,
        Boolean isDeleted
) {
}

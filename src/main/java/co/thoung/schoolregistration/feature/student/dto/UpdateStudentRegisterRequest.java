package co.thoung.schoolregistration.feature.student.dto;

import co.thoung.schoolregistration.domain.Gender;

import java.time.LocalDate;

public record UpdateStudentRegisterRequest(

        String nameKh,
        String nameEng,
        Gender gender,
        LocalDate dob,
        String phoneNumber,
        Integer yearOFStudy,
        String highSchool,

        String houseNo,
        String street,
        String village,
        String commune,
        String district,
        String province,

        String department
) {
}

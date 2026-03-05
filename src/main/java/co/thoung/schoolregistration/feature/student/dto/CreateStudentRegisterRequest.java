package co.thoung.schoolregistration.feature.student.dto;

import co.thoung.schoolregistration.domain.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record CreateStudentRegisterRequest(

        UUID userId,
        String nameKh,
        String nameEng,
        Gender gender,
        LocalDate dob,
        String phoneNumber,
        Integer yearOFStudy,
        String highSchool,
        String houseNo,      // House number
        String street,     // Street name or number
        String village,   // Phum
        String commune,   // Khum / Sangkat
        String district,   // Srok / Khan
        String province ,   //
        String department
) {
}

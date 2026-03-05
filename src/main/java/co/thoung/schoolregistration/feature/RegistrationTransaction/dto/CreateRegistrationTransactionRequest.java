package co.thoung.schoolregistration.feature.RegistrationTransaction.dto;

public record CreateRegistrationTransactionRequest(
        Long registerStudentId,
        String academicYear,
        String semester
) {
}

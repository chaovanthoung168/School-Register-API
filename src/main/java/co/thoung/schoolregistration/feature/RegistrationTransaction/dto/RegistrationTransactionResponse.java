package co.thoung.schoolregistration.feature.RegistrationTransaction.dto;

public record RegistrationTransactionResponse(
    Long id,
    String semester,
    String academicYear,
    Integer registerStudentId,
    Boolean status,
    Boolean isDeleted
) {
}

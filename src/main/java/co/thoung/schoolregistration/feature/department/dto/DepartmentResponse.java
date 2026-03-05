package co.thoung.schoolregistration.feature.department.dto;

public record DepartmentResponse(
    Long id,
    String code,
    String name,
    Boolean isDeleted,
    Double price
) {
}

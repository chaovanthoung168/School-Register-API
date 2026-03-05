package co.thoung.schoolregistration.feature.department.dto;

public record CreateDepartmentRequest(
        String code,
        String name,
        Double price
) {
}

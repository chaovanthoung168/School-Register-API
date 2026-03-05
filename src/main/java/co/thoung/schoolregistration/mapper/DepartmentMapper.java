package co.thoung.schoolregistration.mapper;

import co.thoung.schoolregistration.domain.Department;
import co.thoung.schoolregistration.feature.department.dto.CreateDepartmentRequest;
import co.thoung.schoolregistration.feature.department.dto.DepartmentResponse;
import co.thoung.schoolregistration.feature.department.dto.UpdateDepartmentRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentResponse departmentToDepartmentResponse(Department department);
    Department departmentRequestToDepartment(CreateDepartmentRequest createDepartmentRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentRequestPartially(UpdateDepartmentRequest updateDepartmentRequest,
                                          @MappingTarget Department department);

}

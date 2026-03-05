package co.thoung.schoolregistration.feature.department;

import co.thoung.schoolregistration.feature.department.dto.CreateDepartmentRequest;
import co.thoung.schoolregistration.feature.department.dto.DepartmentResponse;
import co.thoung.schoolregistration.feature.department.dto.UpdateDepartmentRequest;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentResponse createNew(CreateDepartmentRequest createDepartmentRequest);

    List<DepartmentResponse> getDepartment();

    DepartmentResponse updateDepartment(Long uuid, UpdateDepartmentRequest updateDepartmentRequest);

    DepartmentResponse disableDapartment(Long id);

    DepartmentResponse enableDaprtment(Long id);

    DepartmentResponse getDapartmentById(Long id);
}

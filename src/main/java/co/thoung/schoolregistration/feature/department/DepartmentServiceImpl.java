package co.thoung.schoolregistration.feature.department;

import co.thoung.schoolregistration.domain.Department;
import co.thoung.schoolregistration.feature.department.dto.CreateDepartmentRequest;
import co.thoung.schoolregistration.feature.department.dto.DepartmentResponse;
import co.thoung.schoolregistration.feature.department.dto.UpdateDepartmentRequest;
import co.thoung.schoolregistration.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createNew(CreateDepartmentRequest createDepartmentRequest) {

        // validation code
        if (departmentRepository.existsByCode(createDepartmentRequest.code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code already exists");
        }

        // validation department name
        if (departmentRepository.existsByName(createDepartmentRequest.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already exists");
        }

        Department department = departmentMapper.departmentRequestToDepartment(createDepartmentRequest);
        department.setIsDeleted(false);
        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.departmentToDepartmentResponse(savedDepartment);
    }

    @Override
    public List<DepartmentResponse> getDepartment() {
        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No department found");
        }
        return departments.stream()
                .filter(department -> department.getIsDeleted() == false)
                .map(departmentMapper::departmentToDepartmentResponse)
                .toList();
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest updateDepartmentRequest) {

        // validation id
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")
        );

        // validation department name
        if (departmentRepository.existsByName(updateDepartmentRequest.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already exists");
        }
        if (departmentRepository.existsByCode(updateDepartmentRequest.code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code already exists");
        }

        departmentMapper.updateDepartmentRequestPartially(updateDepartmentRequest, department);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.departmentToDepartmentResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse disableDapartment(Long id) {

        // validation id
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")
        );
        department.setIsDeleted(true);
        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.departmentToDepartmentResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse enableDaprtment(Long id) {
        // validation id
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")
        );
        department.setIsDeleted(false);
        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.departmentToDepartmentResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse getDapartmentById(Long id) {

        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")
        );
        return departmentMapper.departmentToDepartmentResponse(department);
    }
}

package co.thoung.schoolregistration.feature.department;

import co.thoung.schoolregistration.feature.department.dto.CreateDepartmentRequest;
import co.thoung.schoolregistration.feature.department.dto.DepartmentResponse;
import co.thoung.schoolregistration.feature.department.dto.UpdateDepartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // create new department
    @PostMapping
    DepartmentResponse createDepartment(@RequestBody CreateDepartmentRequest createDepartmentRequest) {
        return departmentService.createNew(createDepartmentRequest);
    }

    // get department
    @GetMapping
    List<DepartmentResponse> getDepartment(){
        return departmentService.getDepartment();
    }

    // update department
    @PutMapping("/{id}")
    DepartmentResponse updateDepartment(@PathVariable Long id, @RequestBody UpdateDepartmentRequest updateDepartmentRequest) {
        return departmentService.updateDepartment(id,updateDepartmentRequest);
    }

    // disable department
    @GetMapping("/{id}/disable")
    DepartmentResponse disableDepartment(@PathVariable Long id) {
        return departmentService.disableDapartment(id);
    }

    // enable department
     @GetMapping("/{id}/enable")
    DepartmentResponse enableDepartment(@PathVariable Long id) {
        return departmentService.enableDaprtment(id);
     }

    // find by id
    @GetMapping("/{id}")
    DepartmentResponse getDepartmentById(@PathVariable Long id) {
        return departmentService.getDapartmentById(id);
    }
}

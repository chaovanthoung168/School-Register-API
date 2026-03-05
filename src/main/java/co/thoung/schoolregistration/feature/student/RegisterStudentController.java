package co.thoung.schoolregistration.feature.student;

import co.thoung.schoolregistration.feature.student.dto.CreateStudentRegisterRequest;
import co.thoung.schoolregistration.feature.student.dto.RegisterStudentResponse;
import co.thoung.schoolregistration.feature.student.dto.UpdateStudentRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register-student")
public class RegisterStudentController {
    private final RegisterStudentService registerStudentService;

    // create new
    @PostMapping
    RegisterStudentResponse createNewStudent(@RequestBody CreateStudentRegisterRequest  createStudentRegisterRequest){
        return registerStudentService.createNew(createStudentRegisterRequest);
    }

    // find all
    @GetMapping
    List<RegisterStudentResponse> findAllRegisterStudents(){
        return registerStudentService.findAll();
    }

    // update
    @PatchMapping("/{id}")
    RegisterStudentResponse updateById(@PathVariable String id,
                                       @RequestBody UpdateStudentRegisterRequest updateStudentRegisterRequest){
        return registerStudentService.updateById(id,updateStudentRegisterRequest);
    }

    @GetMapping("/{id}/disable")
    RegisterStudentResponse disableStudent(@PathVariable String id){
        return registerStudentService.disable(id);
    }

    @GetMapping("/{id}/enable")
    RegisterStudentResponse enableStudent(@PathVariable String id){
        return registerStudentService.enable(id);
    }

}

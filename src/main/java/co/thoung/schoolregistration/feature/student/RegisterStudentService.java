package co.thoung.schoolregistration.feature.student;

import co.thoung.schoolregistration.feature.student.dto.CreateStudentRegisterRequest;
import co.thoung.schoolregistration.feature.student.dto.RegisterStudentResponse;
import co.thoung.schoolregistration.feature.student.dto.UpdateStudentRegisterRequest;

import java.util.List;

public interface RegisterStudentService {
    RegisterStudentResponse createNew(CreateStudentRegisterRequest createStudentRegisterRequest);

    List<RegisterStudentResponse> findAll();

    RegisterStudentResponse updateById(String id, UpdateStudentRegisterRequest updateStudentRegisterRequest);

    RegisterStudentResponse disable(String id);

    RegisterStudentResponse enable(String id);
}

package co.thoung.schoolregistration.feature.student;

import co.thoung.schoolregistration.domain.Address;
import co.thoung.schoolregistration.domain.Department;
import co.thoung.schoolregistration.domain.RegisterStudent;
import co.thoung.schoolregistration.domain.User;
import co.thoung.schoolregistration.feature.address.AddressRepository;
import co.thoung.schoolregistration.feature.department.DepartmentRepository;
import co.thoung.schoolregistration.feature.student.dto.CreateStudentRegisterRequest;
import co.thoung.schoolregistration.feature.student.dto.RegisterStudentResponse;
import co.thoung.schoolregistration.feature.student.dto.UpdateStudentRegisterRequest;
import co.thoung.schoolregistration.feature.user.UserRepository;
import co.thoung.schoolregistration.mapper.RegisterStudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterStudentServiceImpl implements RegisterStudentService {

    private final RegisterStudentRepository registerStudentRepository;
    private final RegisterStudentMapper registerStudentMapper;
    private final DepartmentRepository departmentRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public RegisterStudentResponse createNew(CreateStudentRegisterRequest createStudentRegisterRequest) {

        // validation user
        User user = userRepository.findByUserId(createStudentRegisterRequest.userId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        // validate department
        Department department = departmentRepository.findByName(createStudentRegisterRequest.department()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")
        );

        // address
        Address address = new Address();
        address.setStreet(createStudentRegisterRequest.street());
        address.setDistrict(createStudentRegisterRequest.district());
        address.setProvince(createStudentRegisterRequest.province());
        address.setVillage(createStudentRegisterRequest.village());
        address.setCommune(createStudentRegisterRequest.commune());
        address.setHouseNo(createStudentRegisterRequest.houseNo());

        // register
        RegisterStudent registerStudent = new RegisterStudent();
        registerStudent.setUser(user);
        registerStudent.setNameEng(createStudentRegisterRequest.nameEng());
        registerStudent.setNameKh(createStudentRegisterRequest.nameKh());
        registerStudent.setStudentNo("SRQ-" + UUID.randomUUID());
        registerStudent.setGender(createStudentRegisterRequest.gender());
        registerStudent.setCreatedAt(LocalDate.now());
        registerStudent.setDob(createStudentRegisterRequest.dob());
        registerStudent.setPhoneNumber(createStudentRegisterRequest.phoneNumber());
        registerStudent.setYearOFStudy(createStudentRegisterRequest.yearOFStudy());
        registerStudent.setHighSchool(createStudentRegisterRequest.highSchool());
        registerStudent.setIsDeleted(false);
        registerStudent.setDepartment(department);

        registerStudent.setAddress(address);
        address.setRegisterStudent(registerStudent);

        RegisterStudent savedStudent = registerStudentRepository.save(registerStudent);

        return registerStudentMapper.studentToStudentResponse(savedStudent);
    }

    @Override
    public List<RegisterStudentResponse> findAll() {
        List<RegisterStudent> registerStudents = registerStudentRepository.findAll();

        if (registerStudents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return registerStudents.stream().filter(r -> r.getIsDeleted() ==  false)
                .map(registerStudentMapper::studentToStudentResponse)
                .toList();
    }

    @Override
    public RegisterStudentResponse updateById(String id, UpdateStudentRegisterRequest updateStudentRegisterRequest) {

        // validation id student
        RegisterStudent registerStudent = registerStudentRepository.findByStudentNo(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found"
                )
        );

        // validation department
        if (updateStudentRegisterRequest.department() != null) {
            Department department = departmentRepository
                    .findByName(updateStudentRegisterRequest.department())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Department not found"));

            registerStudent.setDepartment(department);
        }
        registerStudentMapper.updateStudentFromDto(updateStudentRegisterRequest, registerStudent);

        // update address
        Address address = registerStudent.getAddress();
        if (address == null) {
            address = new Address();
            address.setRegisterStudent(registerStudent);
            registerStudent.setAddress(address);
        }
        registerStudentMapper.updateAddressFromDto(updateStudentRegisterRequest, address);
        RegisterStudent savedStudent = registerStudentRepository.save(registerStudent);
        return registerStudentMapper.studentToStudentResponse(savedStudent);
    }

    @Override
    public RegisterStudentResponse disable(String id) {
        RegisterStudent registerStudent = registerStudentRepository.findByStudentNo(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found")
        );
        registerStudent.setIsDeleted(true);

        return registerStudentMapper.studentToStudentResponse(registerStudentRepository.save(registerStudent));
    }

    @Override
    public RegisterStudentResponse enable(String id) {
        RegisterStudent registerStudent = registerStudentRepository.findByStudentNo(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found")
        );
        registerStudent.setIsDeleted(false);

        return registerStudentMapper.studentToStudentResponse(registerStudentRepository.save(registerStudent));
    }
}

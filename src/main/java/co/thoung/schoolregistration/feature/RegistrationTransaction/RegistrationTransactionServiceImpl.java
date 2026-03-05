package co.thoung.schoolregistration.feature.RegistrationTransaction;


import co.thoung.schoolregistration.domain.RegisterStudent;
import co.thoung.schoolregistration.domain.RegistrationTransaction;
import co.thoung.schoolregistration.feature.RegistrationTransaction.dto.CreateRegistrationTransactionRequest;
import co.thoung.schoolregistration.feature.RegistrationTransaction.dto.RegistrationTransactionResponse;
import co.thoung.schoolregistration.feature.student.RegisterStudentRepository;
import co.thoung.schoolregistration.mapper.RegistrationTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationTransactionServiceImpl implements RegistrationTransactionService {

    private final RegisterStudentRepository registerStudentRepository;
    private final RegistrationTransactionRepository registrationTransactionRepository;
    private final RegistrationTransactionMapper registrationTransactionMapper;

    @Override
    public RegistrationTransactionResponse createRegistrationTransaction(CreateRegistrationTransactionRequest request) {

        // validation register student
        RegisterStudent registerStudent = registerStudentRepository.findById(request.registerStudentId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Register Student Not Found")
        );

        RegistrationTransaction registrationTransaction = new RegistrationTransaction();
        registrationTransaction.setRegisterStudent(registerStudent);
        registrationTransaction.setCreatedAt(LocalDate.now());
        registrationTransaction.setSemester(request.semester());
        registrationTransaction.setAcademicYear(request.academicYear());
        registrationTransaction.setStatus(false);
        registrationTransaction.setIsDeleted(false);
        registrationTransactionRepository.save(registrationTransaction);

        RegistrationTransaction savedRegistrationTransaction = registrationTransactionRepository.save(registrationTransaction);

        return registrationTransactionMapper.toRegistrationResponse(savedRegistrationTransaction);
    }

    @Override
    public List<RegistrationTransactionResponse> findAll() {

            List<RegistrationTransaction> registrationTransactions = registrationTransactionRepository.findAll();

            if (registrationTransactions.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration Transactions Not Found");
            }

        return registrationTransactions.stream()
                .filter(r -> r.getIsDeleted() == false)
                .map(registrationTransactionMapper::toRegistrationResponse)
                .toList();
    }

    @Override
    public RegistrationTransactionResponse findById(Integer id) {

        RegistrationTransaction registrationTransaction = registrationTransactionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration Transaction Not Found")
        );

        return registrationTransactionMapper.toRegistrationResponse(registrationTransaction);
    }

    @Override
    public RegistrationTransactionResponse disable(Integer id) {

        RegistrationTransaction registrationTransaction = registrationTransactionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration Transaction Not Found")
        );
        registrationTransaction.setIsDeleted(true);
        return registrationTransactionMapper.toRegistrationResponse(registrationTransactionRepository.save(registrationTransaction));
    }

    @Override
    public RegistrationTransactionResponse enable(Integer id) {
        RegistrationTransaction registrationTransaction = registrationTransactionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration Transaction Not Found")
        );
        registrationTransaction.setIsDeleted(false);
        return registrationTransactionMapper.toRegistrationResponse(registrationTransactionRepository.save(registrationTransaction));
    }
}

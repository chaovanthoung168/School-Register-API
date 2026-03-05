package co.thoung.schoolregistration.feature.RegistrationTransaction;

import co.thoung.schoolregistration.domain.RegistrationTransaction;
import co.thoung.schoolregistration.feature.RegistrationTransaction.dto.CreateRegistrationTransactionRequest;
import co.thoung.schoolregistration.feature.RegistrationTransaction.dto.RegistrationTransactionResponse;
import co.thoung.schoolregistration.feature.student.dto.CreateStudentRegisterRequest;
import co.thoung.schoolregistration.feature.student.dto.RegisterStudentResponse;

import java.util.List;
import java.util.UUID;

public interface RegistrationTransactionService {

    RegistrationTransactionResponse createRegistrationTransaction(CreateRegistrationTransactionRequest request);

    List<RegistrationTransactionResponse> findAll();

    RegistrationTransactionResponse findById(Integer id);

    RegistrationTransactionResponse disable(Integer id);
    RegistrationTransactionResponse enable(Integer id);
}

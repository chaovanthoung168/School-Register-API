package co.thoung.schoolregistration.feature.RegistrationTransaction;

import co.thoung.schoolregistration.feature.RegistrationTransaction.dto.CreateRegistrationTransactionRequest;
import co.thoung.schoolregistration.feature.RegistrationTransaction.dto.RegistrationTransactionResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Registration-Transaction")
@RequiredArgsConstructor
public class RegistrationTransactionController {

    private final RegistrationTransactionService registrationTransactionService ;

    @PostMapping
    RegistrationTransactionResponse createRegistrationTransaction(@RequestBody CreateRegistrationTransactionRequest createRegistrationTransactionRequest) {
        return registrationTransactionService.createRegistrationTransaction(createRegistrationTransactionRequest);
    }

    @GetMapping
    List<RegistrationTransactionResponse> getRegistrationTransactions() {
        return registrationTransactionService.findAll();
    }

    @GetMapping("/{id}")
    RegistrationTransactionResponse findById(@PathVariable Integer id) {
        return registrationTransactionService.findById(id);
    }

    @GetMapping("/{id}/disable")
    RegistrationTransactionResponse disable(@PathVariable Integer id) {
        return registrationTransactionService.disable(id);
    }

    @GetMapping("/{id}/enable")
    RegistrationTransactionResponse enable(@PathVariable Integer id) {
        return registrationTransactionService.enable(id);
    }
}

package co.thoung.schoolregistration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "payments")
public class PaymentManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "payment_ref is required")
    private String paymentRef;

    @NotNull(message = "amount is required")
    private Double amount;

    @NotNull(message = "paymentMethod is required")
    private String paymentMethod;

    @Column(name = "payment_type")
    private String paymentType;

    @NotNull(message = "createdAt is required")
    private LocalDate createdAt;

    @OneToOne
    @JoinColumn(name = "registration_id")
    private RegistrationTransaction registrationTransaction;

}

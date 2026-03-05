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
@Table(name = "registration_transation")
public class RegistrationTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String semester;
    @NotBlank
    private String academicYear;
    @NotNull
    private Boolean status;

    private Boolean isDeleted;

    @NotNull
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private RegisterStudent registerStudent;

    @OneToOne(mappedBy = "registrationTransaction", cascade = CascadeType.ALL)
    private PaymentManagement payment;
}

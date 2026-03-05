package co.thoung.schoolregistration.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "register_students")
public class RegisterStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "student is required")
    private String studentNo;

    @NotBlank(message = "nameKh is required")
    private String nameKh;

    @NotBlank(message = "nameEng is required")
    private String nameEng;

    @NotNull(message = "gender is required")
    private Gender gender;

    @NotNull(message = "dob is required")
    private LocalDate dob;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotNull(message = "year of study is required")
    private Integer yearOFStudy;

    LocalDate createdAt;

    private String highSchool;
    private Boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "registerStudent", cascade = CascadeType.ALL)
    private List<RegistrationTransaction> registrations;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}

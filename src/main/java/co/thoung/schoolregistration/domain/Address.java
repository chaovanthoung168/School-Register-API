package co.thoung.schoolregistration.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String houseNo;      // House number
    private String street;       // Street name or number
    private String village;      // Phum
    private String commune;      // Khum / Sangkat
    private String district;     // Srok / Khan
    private String province;     // Province or Phnom Penh

    @OneToOne(mappedBy = "address")
    private RegisterStudent registerStudent;
}

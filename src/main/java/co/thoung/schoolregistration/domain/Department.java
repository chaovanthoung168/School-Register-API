package co.thoung.schoolregistration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "code is required")
    private String code;
    @NotBlank(message = "name is required")
    private String name;

    private Boolean isDeleted;

    private Double price;

    // One Department → Many Students
    @OneToMany(mappedBy = "department")
    private List<RegisterStudent> registerStudents;
}

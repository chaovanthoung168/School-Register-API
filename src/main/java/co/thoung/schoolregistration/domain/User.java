package co.thoung.schoolregistration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private UUID userId;

    @NotBlank(message = "username is required")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false, length = 100)
    private String password;

    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean isVerified;

    @OneToMany
    @JoinColumn(name = "created_by")
    private List<RegistrationTransaction> registrations;

    @OneToMany(mappedBy = "user")
    private List<RegisterStudent> registerStudents;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),        // ✅ id not user_id
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")  // ✅ role's id
    )
    private List<Role> roles;
}


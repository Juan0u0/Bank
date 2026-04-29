package app.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document", unique = true, nullable = false)
    private String document;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "adress")
    private String adress;

    @Column(name = "client_type", insertable = false, updatable = false)
    private String clientType;

    // NaturalPerson fields
    @Column(name = "birth_date")
    private LocalDate birthDate;

    // Company fields
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "legal_representative")
    private String legalRepresentative;

    @Column(name = "role")
    private String role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

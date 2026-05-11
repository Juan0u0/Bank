package app.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company_clients")
public class CompanyClientEntity extends BaseClientEntity {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "legal_representative")
    private String legalRepresentative;

    @Column(name = "role")
    private String role;
}

package app.domain.models;

import app.domain.enums.SistemRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Company extends Client {
    private String companyName;       // Razón social oficial de la empresa
    private String taxId;              // NIT o identificación fiscal
    private String legalRepresentative; // Nombre del representante legal
    private SistemRole role;           // COMPANY, COMPANY_EMPLOYEE, COMPANY_SUPERVISOR
}

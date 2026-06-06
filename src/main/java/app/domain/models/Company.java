package app.domain.models;

import app.domain.enums.sistemRoles.SistemRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Company extends Client {
    private String companyName;       // Razón social oficial de la empresa
    private String legalRepresentative; // Nombre del representante legal
    private SistemRole role;          // Rol de la empresa (CLIENT_COMPANY, COMPANY_EMPLOYEE, etc)
}

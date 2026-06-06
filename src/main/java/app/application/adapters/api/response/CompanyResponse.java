package app.application.adapters.api.response;

import app.domain.enums.sistemRoles.SistemRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponse {
    private String document;
    private String name;
    private String companyName;
    private String taxId;
    private String legalRepresentative;
    private String email;
    private String cellPhone;
    private String adress;
    private SistemRole role;
}

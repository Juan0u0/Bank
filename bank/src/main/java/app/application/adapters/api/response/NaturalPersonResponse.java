package app.application.adapters.api.response;

import app.domain.enums.sistemRoles.SistemRole;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class NaturalPersonResponse {
    private String document;
    private String name;
    private String email;
    private String cellPhone;
    private String adress;
    private LocalDate birthDate;
    private SistemRole role;
}


package app.domain.models;

import app.domain.enums.SistemRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Company extends Client {

    private SistemRole companyType;
    
}

package app.domain.models;

import app.domain.enums.sistemRoles.SistemRole;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class NaturalPerson extends Client {
    private LocalDate birthDate;
    private SistemRole role;
}


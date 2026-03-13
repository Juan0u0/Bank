package app.domain.models;

import org.springframework.cglib.core.Local;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class NaturalPerson extends Client {
    private Local birthDate;
}

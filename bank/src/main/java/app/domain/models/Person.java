package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public abstract class Person {
    private String name;
    private String document;
    private String email;
    private String cellPhone;
    private String adress;
}

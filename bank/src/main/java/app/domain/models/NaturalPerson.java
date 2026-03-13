package app.domain.models;

import org.springframework.cglib.core.Local;

public class NaturalPerson extends Client {
    
    private String name;
    private String documentNumber;
    private Local birthDate;
}

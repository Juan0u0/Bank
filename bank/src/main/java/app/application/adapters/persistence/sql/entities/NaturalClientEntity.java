package app.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "natural_clients")
public class NaturalClientEntity extends BaseClientEntity {

    @Column(name = "birth_date")
    private LocalDate birthDate;
}

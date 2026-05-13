package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {
    
    @NotBlank(message = "El documento (NIT) es obligatorio")
    private String nit;

    @NotBlank(message = "El nombre de la razón social es obligatorio")
    private String name;

    @NotBlank(message = "La razón social es obligatoria")
    private String companyName;

    @NotBlank(message = "El representante legal es obligatorio")
    private String legalRepresentative;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 7, max = 15, message = "El teléfono debe tener entre 7 y 15 dígitos")
    private String cellPhone;

    @NotBlank(message = "La dirección es obligatoria")
    private String adress;

    private String role;
}

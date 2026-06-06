package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    
    @NotBlank(message = "El documento es obligatorio")
    private String document;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    private String role;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 7, max = 15, message = "El teléfono debe tener entre 7 y 15 dígitos")
    private String cellPhone;

    @NotBlank(message = "La dirección es obligatoria")
    private String adress;

    // Campos adicionales para registro de nuevos usuarios
    @Pattern(regexp = "NATURAL_PERSON|COMPANY", message = "El tipo de usuario debe ser NATURAL_PERSON o COMPANY")
    private String userType;

    @Pattern(regexp = "^(\\d{4})-(\\d{2})-(\\d{2})$", message = "La fecha de nacimiento debe estar en formato YYYY-MM-DD")
    private String birthDate;
}

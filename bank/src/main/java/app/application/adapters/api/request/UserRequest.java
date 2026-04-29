package app.application.adapters.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100)
    String fullName;

    @NotBlank(message = "La identificación es obligatoria")
    String identificationId;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 7, max = 15)
    String phone;

    @NotBlank(message = "La dirección es obligatoria")
    String address;

    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Formato DD/MM/YYYY")
    String birthDate;
    
    @NotBlank(message = "El rol es obligatorio")
    String systemRole; // Ejemplo: INTERNAL_ANALYST, WINDOW_EMPLOYEE
    
    @NotBlank(message = "El estado es obligatorio")
    String userStatus; // Activo, Inactivo, Bloqueado
    String relatedEntityId; // NIT de la empresa si aplica
}

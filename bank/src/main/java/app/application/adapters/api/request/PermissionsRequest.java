package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PermissionsRequest {
    
    @NotBlank(message = "El documento del usuario es obligatorio")
    private String userDocument;
    
    @NotEmpty(message = "Debe especificar al menos un permiso")
    private Set<String> permissions;
}

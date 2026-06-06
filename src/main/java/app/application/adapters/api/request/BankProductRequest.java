package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankProductRequest {
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String productName;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    @NotBlank(message = "El estado es obligatorio")
    private String status;
}

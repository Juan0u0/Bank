package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProductApplicationRequest {
    
    @NotBlank(message = "El documento del cliente es obligatorio")
    private String clientDocument;
    
    @NotBlank(message = "El tipo de producto es obligatorio")
    private String productType; // LOAN, ACCOUNT, etc.
    
    @NotNull(message = "Los detalles del producto son obligatorios")
    private Map<String, Object> productDetails;
}

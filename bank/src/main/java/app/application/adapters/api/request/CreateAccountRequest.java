package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {
    
    @NotBlank(message = "El documento del cliente es obligatorio")
    private String clientDocument;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String accountType;

    @NotNull(message = "El saldo es obligatorio")
    @Positive(message = "El saldo debe ser mayor a cero")
    private BigDecimal balance;

    @NotBlank(message = "La moneda es obligatoria")
    private String currency;
}

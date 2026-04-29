package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    
    @NotBlank(message = "La cuenta origen es obligatoria")
    private String originAccountNumber;

    @NotBlank(message = "La cuenta destino es obligatoria")
    private String destinationAccountNumber;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal amount;
}

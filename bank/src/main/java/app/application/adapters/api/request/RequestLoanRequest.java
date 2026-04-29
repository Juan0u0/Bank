package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class RequestLoanRequest {
    
    @NotBlank(message = "El tipo de préstamo es obligatorio")
    private String loanType;

    @NotNull(message = "El monto solicitado es obligatorio")
    @Positive(message = "El monto solicitado debe ser mayor a cero")
    private BigDecimal amountRequested;

    @NotNull(message = "La tasa de interés es obligatoria")
    @Positive(message = "La tasa de interés debe ser mayor a cero")
    private BigDecimal interestRate;

    @NotNull(message = "El plazo es obligatorio")
    @Positive(message = "El plazo debe ser mayor a cero")
    private Integer term;
}

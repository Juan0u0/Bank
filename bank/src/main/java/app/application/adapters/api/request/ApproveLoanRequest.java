package app.application.adapters.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ApproveLoanRequest {
    
    @NotNull(message = "El ID del préstamo es obligatorio")
    @Positive(message = "El ID del préstamo debe ser mayor a cero")
    private Long loanId;

    @NotNull(message = "El monto aprobado es obligatorio")
    @Positive(message = "El monto aprobado debe ser mayor a cero")
    private BigDecimal amountApproved;
}

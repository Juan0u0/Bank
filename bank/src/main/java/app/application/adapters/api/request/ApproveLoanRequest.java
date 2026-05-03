package app.application.adapters.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ApproveLoanRequest {
    
    @NotNull(message = "El ID del préstamo es obligatorio")
    @Positive(message = "El ID del préstamo debe ser mayor a cero")
    private Long loanId;

    @NotBlank(message = "La acción es obligatoria")
    @Pattern(regexp = "APPROVE|REJECT", message = "La acción debe ser APPROVE o REJECT")
    private String action;

    @Positive(message = "El monto aprobado debe ser mayor a cero")
    private BigDecimal amountApproved;
}

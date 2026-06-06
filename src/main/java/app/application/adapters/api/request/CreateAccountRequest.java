package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import app.domain.enums.status.AccountType;
import app.domain.enums.status.Currency;

@Getter
@Setter
public class CreateAccountRequest {
    
    @NotBlank(message = "El documento del cliente es obligatorio")
    private String clientDocument;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private AccountType accountType;

    @NotNull(message = "La moneda es obligatoria")
    private Currency currency;
}

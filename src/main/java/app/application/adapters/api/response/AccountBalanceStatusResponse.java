package app.application.adapters.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountBalanceStatusResponse {
    private BigDecimal balance;
    private String accountStatus;
}

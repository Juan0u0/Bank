package app.application.adapters.api.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BankAccountResponse {
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private String accountStatus;
    private LocalDateTime openingDate;
    private String clientDocument;
}

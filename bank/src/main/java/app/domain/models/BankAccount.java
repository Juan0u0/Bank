package app.domain.models;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;
import app.domain.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class BankAccount {
    private String accountNumber;
    private AccountType accountType;
    private Client client; //Cliente asociado a la cuenta
    private BigDecimal balance;
    private Currency currency;
    private AccountStatus accountStatus;
    private LocalDateTime openingDate;
}

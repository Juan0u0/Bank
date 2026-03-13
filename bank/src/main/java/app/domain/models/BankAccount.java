package app.domain.models;

import java.time.LocalDate;


import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;
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
    private Long balance;
    private String coin;
    private AccountStatus accountStatus;
    private LocalDate openingDate;
}

package app.domain.models;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Loan {
    private Long loanId;
    private LoanType loanType;
    private Client client;
    private BigDecimal amountRequested; //Monto solicitado
    private BigDecimal amountApproved; //Monto aprobado
    private BigDecimal interestRate; //Tasa de interés
    private Integer term; //Plazo en meses
    private LoanStatus loanStatus;
    private LocalDateTime approvalDate; //Fecha de aprobación
    private LocalDateTime disbursementDate; //Fecha de desembolso
    private BankAccount bankAccount; //Cuenta bancaria asociada al préstamo
}

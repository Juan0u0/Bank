package app.domain.models;

import java.time.LocalDate;


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
    private Long amountRequested; //Monto solicitado
    private Long amountApproved; //Monto aprobado
    private Long interestRate; //Tasa de interés
    private Integer term; //Plazo en meses
    private LoanStatus loanStatus;
    private LocalDate approvalDate; //Fecha de aprobación
    private LocalDate disbursementDate; //Fecha de desembolso
    private BankAccount bankAccount; //Cuenta bancaria asociada al préstamo
    
}

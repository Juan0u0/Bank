package app.domain.models;

import java.time.LocalDate;


import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;

public class Loan {
    private Long loanId;
    private LoanType loanType;
    private Client client;
    private Long amountRequested; //Monto solicitado
    private Long amountApproved; //Monto aprobado
    private Long interest_rate; //Tasa de interés
    private Integer term; //Plazo en meses
    private LoanStatus loanStatus;
    private LocalDate approvalDate; //Fecha de aprobación
    private LocalDate disbursementDate; //Fecha de desembolso
    private BankAccount bankAccount; //Cuenta bancaria asociada al préstamo
    
}

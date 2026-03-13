package app.domain.models;

import java.time.LocalDate;

import app.domain.enums.TransferStatus;

public class Transfer {
    private Long transferId;
    private BankAccount originAccount;
    private BankAccount destinationAccount;
    private Long amount;
    private LocalDate creationDate;
    private LocalDate approvalDate; //Feca aprobación
    private TransferStatus transferStatus;
    private User createdBy; //Usuario que creó la transferencia
    private User approvedBy; //Usuario que aprobó la transferencia
}

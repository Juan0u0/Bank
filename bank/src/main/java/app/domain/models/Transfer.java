package app.domain.models;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import app.domain.enums.TransferStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Transfer {
    private Long transferId;
    private BankAccount originAccount;
    private BankAccount destinationAccount;
    private BigDecimal amount;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate; //Feca aprobación
    private TransferStatus transferStatus;
    private User createdBy; //Usuario que creó la transferencia
    private User approvedBy; //Usuario que aprobó la transferencia
}

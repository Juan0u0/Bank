package app.application.adapters.api.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransferResponse {
    private Long transferId;
    private String originAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate;
    private String transferStatus;
    private String createdByDocument;
    private String approvedByDocument;
}

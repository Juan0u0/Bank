package app.application.adapters.api.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LoanResponse {
    private Long loanId;
    private String loanType;
    private String clientDocument;
    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    private Integer term;
    private String loanStatus;
    private LocalDateTime approvalDate;
    private LocalDateTime disbursementDate;
}

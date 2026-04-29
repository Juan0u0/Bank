package app.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "loans")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @Column(name = "loan_type", nullable = false)
    private String loanType;

    @Column(name = "client_document", nullable = false)
    private String clientDocument;

    @Column(name = "amount_requested", nullable = false)
    private BigDecimal amountRequested;

    @Column(name = "amount_approved")
    private BigDecimal amountApproved;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "term", nullable = false)
    private Integer term;

    @Column(name = "loan_status", nullable = false)
    private String loanStatus;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "disbursement_date")
    private LocalDateTime disbursementDate;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

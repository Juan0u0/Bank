package app.application.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transfers")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @Column(name = "origin_account_number", nullable = false)
    private String originAccountNumber;

    @Column(name = "destination_account_number", nullable = false)
    private String destinationAccountNumber;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "transfer_status", nullable = false)
    private String transferStatus;

    @Column(name = "created_by_document")
    private String createdByDocument;

    @Column(name = "approved_by_document")
    private String approvedByDocument;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

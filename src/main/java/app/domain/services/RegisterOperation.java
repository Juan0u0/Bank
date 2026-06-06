package app.domain.services;

import app.domain.models.OperationLog;
import app.domain.ports.OperationLogPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RegisterOperation {
    
    private final OperationLogPort operationLogPort;
    
    public static final String OP_ACCOUNT_OPENED = "ACCOUNT_OPENED";
    public static final String OP_ACCOUNT_CLOSED = "ACCOUNT_CLOSED";
    public static final String OP_ACCOUNT_BLOCKED = "ACCOUNT_BLOCKED";
    public static final String OP_DEPOSIT = "DEPOSIT";
    public static final String OP_WITHDRAW = "WITHDRAW";
    public static final String OP_TRANSFER_CREATED = "TRANSFER_CREATED";
    public static final String OP_TRANSFER_APPROVED = "TRANSFER_APPROVED";
    public static final String OP_TRANSFER_EXECUTED = "TRANSFER_EXECUTED";
    public static final String OP_TRANSFER_EXPIRED = "TRANSFER_EXPIRED";
    public static final String OP_LOAN_REQUESTED = "LOAN_REQUESTED";
    public static final String OP_LOAN_APPROVED = "LOAN_APPROVED";
    public static final String OP_LOAN_DISBURSED = "LOAN_DISBURSED";
    
    @Autowired
    public RegisterOperation(OperationLogPort operationLogPort) {
        this.operationLogPort = operationLogPort;
    }
    
    public void registerOperation(OperationLog log) {
        operationLogPort.save(log);
    }
    
    public void registerAccountOpened(String userDocument, String accountNumber, String accountType, String currency) {
        OperationLog log = new OperationLog();
        log.setOperationDate(LocalDateTime.now());
        log.setOperationType(OP_ACCOUNT_OPENED);
        log.getDetails().put("accountNumber", accountNumber);
        log.getDetails().put("accountType", accountType);
        log.getDetails().put("currency", currency);
        operationLogPort.save(log);
    }
    
    public void registerAccountClosed(String userDocument, String accountNumber) {
        OperationLog log = new OperationLog();
        log.setOperationDate(LocalDateTime.now());
        log.setOperationType(OP_ACCOUNT_CLOSED);
        log.getDetails().put("accountNumber", accountNumber);
        operationLogPort.save(log);
    }
    
    public void registerAccountBlocked(String userDocument, String accountNumber) {
        OperationLog log = new OperationLog();
        log.setOperationDate(LocalDateTime.now());
        log.setOperationType(OP_ACCOUNT_BLOCKED);
        log.getDetails().put("accountNumber", accountNumber);
        operationLogPort.save(log);
    }
    
    public void registerTransferCreated(String userDocument, String originAccount, String destinationAccount, BigDecimal amount) {
        OperationLog log = new OperationLog();
        log.setOperationType(OP_TRANSFER_CREATED);
        log.setOperationDate(LocalDateTime.now());
        log.getDetails().put("originAccount", originAccount);
        log.getDetails().put("destinationAccount", destinationAccount);
        log.getDetails().put("amount", amount);
        operationLogPort.save(log);
    }
    
    public void registerTransferApproved(String userDocument, Long transferId, BigDecimal amount) {
        OperationLog log = new OperationLog();
        log.setOperationType(OP_TRANSFER_APPROVED);
        log.setOperationDate(LocalDateTime.now());
        log.getDetails().put("transferId", transferId);
        log.getDetails().put("amount", amount);
        operationLogPort.save(log);
    }
    
    public void registerTransferExecuted(String userDocument, String originAccount, BigDecimal balanceBeforeOrigin, BigDecimal balanceAfterOrigin,
                                        String destinationAccount, BigDecimal balanceBeforeDestination, BigDecimal balanceAfterDestination,
                                        BigDecimal amount) {
        OperationLog log = new OperationLog();
        log.setOperationDate(LocalDateTime.now());
        log.setOperationType(OP_TRANSFER_EXECUTED);
        log.getDetails().put("originAccount", originAccount);
        log.getDetails().put("balanceBeforeOrigin", balanceBeforeOrigin);
        log.getDetails().put("balanceAfterOrigin", balanceAfterOrigin);
        log.getDetails().put("destinationAccount", destinationAccount);
        log.getDetails().put("balanceBeforeDestination", balanceBeforeDestination);
        log.getDetails().put("balanceAfterDestination", balanceAfterDestination);
        log.getDetails().put("amount", amount);
        operationLogPort.save(log);
    }
    
    public void registerLoanRequested(String userDocument, Long loanId, BigDecimal amount) {
        OperationLog log = new OperationLog();
        log.setOperationDate(LocalDateTime.now());
        log.setOperationType(OP_LOAN_REQUESTED);
        log.getDetails().put("loanId", loanId);
        log.getDetails().put("amountRequested", amount);
        operationLogPort.save(log);
    }
    
    public void registerLoanApproved(String userDocument, Long loanId, BigDecimal approvedAmount, BigDecimal interestRate) {
        OperationLog log = new OperationLog();
        log.setOperationDate(LocalDateTime.now());
        log.setOperationType(OP_LOAN_APPROVED);
        log.getDetails().put("loanId", loanId);
        log.getDetails().put("amountApproved", approvedAmount);
        log.getDetails().put("interestRate", interestRate);
        operationLogPort.save(log);
    }
    
    public void registerLoanDisbursed(String userDocument, Long loanId, BigDecimal amount, String accountNumber) {
        OperationLog log = new OperationLog();
        log.setOperationType(OP_LOAN_DISBURSED);
        log.setOperationDate(LocalDateTime.now());
        log.getDetails().put("loanId", loanId);
        log.getDetails().put("amountDisbursed", amount);
        log.getDetails().put("destinationAccount", accountNumber);
        operationLogPort.save(log);
    }
    
    public void registerDeposit(String userDocument, String accountNumber, BigDecimal balanceBefore, BigDecimal balanceAfter, BigDecimal amount) {
        OperationLog log = new OperationLog();
        log.setOperationType(OP_DEPOSIT);
        log.setOperationDate(LocalDateTime.now());
        log.getDetails().put("accountNumber", accountNumber);
        log.getDetails().put("balanceBefore", balanceBefore);
        log.getDetails().put("balanceAfter", balanceAfter);
        log.getDetails().put("amount", amount);
        operationLogPort.save(log);
    }
    
    public void registerWithdraw(String userDocument, String accountNumber, BigDecimal balanceBefore, BigDecimal balanceAfter, BigDecimal amount) {
        OperationLog log = new OperationLog();
        log.setOperationType(OP_WITHDRAW);
        log.setOperationDate(LocalDateTime.now());
        log.getDetails().put("accountNumber", accountNumber);
        log.getDetails().put("balanceBefore", balanceBefore);
        log.getDetails().put("balanceAfter", balanceAfter);
        log.getDetails().put("amount", amount);
        operationLogPort.save(log);
    }
    
    public void registerTransferExpired(String transferId, String originAccount, String destinationAccount, BigDecimal amount) {
        OperationLog log = new OperationLog();
        log.setOperationType(OP_TRANSFER_EXPIRED);
        log.setOperationDate(LocalDateTime.now());
        log.getDetails().put("transferId", transferId);
        log.getDetails().put("originAccount", originAccount);
        log.getDetails().put("destinationAccount", destinationAccount);
        log.getDetails().put("amount", amount);
        log.getDetails().put("reason", "Transferencia expirada - tiempo de aprobación excedido (60 minutos)");
        operationLogPort.save(log);
    }
}

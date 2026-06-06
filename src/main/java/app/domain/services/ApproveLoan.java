package app.domain.services;

import app.domain.models.Loan;
import app.domain.models.BankAccount;
import app.domain.enums.approvalFlows.LoanStatus;
import app.domain.enums.sistemRoles.SistemRole;
import app.domain.ports.LoanPort;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class ApproveLoan {
    
    private final LoanPort loanPort;
    private final BankAccountPort accountPort;
    private final RegisterOperation registerOperation;
    
    public ApproveLoan(LoanPort loanPort, BankAccountPort accountPort, RegisterOperation registerOperation) {
        this.loanPort = loanPort;
        this.accountPort = accountPort;
        this.registerOperation = registerOperation;
    }
    
    public void approveLoan(Long loanId, BigDecimal amountApproved, SistemRole userRole) throws BusinessException {
        if (userRole != SistemRole.INTERNAL_ANALYST) {
            throw new BusinessException("Solo un Analista Interno puede aprobar préstamos");
        }
        
        Loan loan = loanPort.findById(loanId);
        if (loan == null) {
            throw new BusinessException("El préstamo no existe");
        }
        
        if (loan.getLoanStatus() != LoanStatus.IN_STUDY) {
            throw new BusinessException("El préstamo solo puede ser aprobado si está en estado 'En estudio'");
        }
        
        if (amountApproved.compareTo(BigDecimal.ZERO) <= 0 || amountApproved.compareTo(loan.getAmountRequested()) > 0) {
            throw new BusinessException("El monto aprobado debe ser mayor a cero y no mayor al solicitado");
        }
        
        // ========== VALIDAR QUE EL CLIENTE TIENE CUENTA ANTES DE APROBAR ==========
        // Buscar cuenta del cliente usando el documento
        String documentToUse = loan.getClientDocument();
        if (documentToUse == null || documentToUse.trim().isEmpty()) {
            // Si no hay clientDocument, intentar obtenerlo del cliente
            if (loan.getClient() != null && loan.getClient().getDocument() != null) {
                documentToUse = loan.getClient().getDocument();
            } else {
                throw new BusinessException("No se puede aprobar: no hay información del cliente en el préstamo");
            }
        }
        
        // Buscar primera cuenta disponible del cliente (no cerrada)
        BankAccount account = accountPort.findFirstActiveByClientDocument(documentToUse.trim());
        if (account == null) {
            throw new BusinessException("El préstamo no puede ser aprobado porque el cliente (documento: " + documentToUse + 
                ") no tiene una cuenta bancaria disponible. " +
                "El cliente debe registrar una cuenta bancaria antes de aprobar el préstamo. " +
                "El estado del préstamo permanecerá en 'En estudio'.");
        }
        
        // ========== APROBAR PRÉSTAMO ==========
        loan.setAmountApproved(amountApproved);
        loan.setLoanStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDateTime.now());
        
        loanPort.update(loan);
        
        // Registrar aprobación en bitácora (NoSQL)
        registerOperation.registerLoanApproved(
            null, // Se obtendrá del contexto de seguridad en la capa de aplicación
            loanId,
            amountApproved,
            loan.getInterestRate()
        );
        
        // ========== DESEMBOLSO AUTOMÁTICO ==========
        
        // Asociar la cuenta al préstamo
        loan.setBankAccount(account);
        
        // Actualizar saldo de la cuenta
        BigDecimal newBalance = account.getBalance().add(amountApproved);
        accountPort.updateBalance(account.getAccountNumber(), newBalance);
        
        // Actualizar estado del préstamo a DESEMBOLSADO
        loan.setLoanStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDateTime.now());
        
        loanPort.update(loan);
        
        // Registrar operación de desembolso en bitácora (NoSQL)
        registerOperation.registerLoanDisbursed(
            loan.getClientDocument(), 
            loanId, 
            amountApproved, 
            account.getAccountNumber()
        );
    }
}


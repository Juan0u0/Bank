package app.domain.services;

import app.domain.models.Loan;
import app.domain.enums.LoanStatus;
import app.domain.enums.SistemRole;
import app.domain.ports.LoanPort;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class DisburseLoan {
    
    private final LoanPort loanPort;
    private final BankAccountPort accountPort;
    
    @Autowired
    public DisburseLoan(LoanPort loanPort, BankAccountPort accountPort) {
        this.loanPort = loanPort;
        this.accountPort = accountPort;
    }
    
    public void disburse(Long loanId, SistemRole userRole) throws BusinessException {
        if (userRole != SistemRole.INTERNAL_ANALYST) {
            throw new BusinessException("Solo un Analista Interno puede desembolsar préstamos");
        }
        
        Loan loan = loanPort.findById(loanId);
        if (loan == null) {
            throw new BusinessException("El préstamo no existe");
        }
        
        if (loan.getLoanStatus() != LoanStatus.APPROVED) {
            throw new BusinessException("El préstamo solo puede ser desembolsado si está aprobado");
        }
        
        if (loan.getBankAccount() == null) {
            throw new BusinessException("La cuenta destino para el desembolso no ha sido definida");
        }
        
        var account = accountPort.findByAccountNumber(loan.getBankAccount().getAccountNumber());
        if (account == null) {
            throw new BusinessException("La cuenta destino para el desembolso no existe");
        }
        
        if (loan.getAmountApproved().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto aprobado debe ser mayor a cero");
        }
        
        // Actualizar saldo de la cuenta
        BigDecimal newBalance = account.getBalance().add(loan.getAmountApproved());
        accountPort.updateBalance(account.getAccountNumber(), newBalance);
        
        // Actualizar estado del préstamo
        loan.setLoanStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDateTime.now());
        
        loanPort.update(loan);
    }
}

package app.domain.services;

import app.domain.models.Loan;
import app.domain.enums.LoanStatus;
import app.domain.enums.SistemRole;
import app.domain.ports.LoanPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class ApproveLoan {
    
    private final LoanPort loanPort;
    
    @Autowired
    public ApproveLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
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
        
        loan.setAmountApproved(amountApproved);
        loan.setLoanStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDateTime.now());
        
        loanPort.update(loan);
    }
}

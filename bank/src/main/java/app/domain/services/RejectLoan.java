package app.domain.services;

import app.domain.models.Loan;
import app.domain.enums.LoanStatus;
import app.domain.enums.SistemRole;
import app.domain.ports.LoanPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RejectLoan {
    
    private final LoanPort loanPort;
    
    @Autowired
    public RejectLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }
    
    public void rejectLoan(Long loanId, SistemRole userRole) throws BusinessException {
        if (userRole != SistemRole.INTERNAL_ANALYST) {
            throw new BusinessException("Solo un Analista Interno puede rechazar préstamos");
        }
        
        Loan loan = loanPort.findById(loanId);
        if (loan == null) {
            throw new BusinessException("El préstamo no existe");
        }
        
        if (loan.getLoanStatus() != LoanStatus.IN_STUDY) {
            throw new BusinessException("El préstamo solo puede ser rechazado si está en estado 'En estudio'");
        }
        
        loan.setLoanStatus(LoanStatus.REJECTED);
        loanPort.update(loan);
    }
}

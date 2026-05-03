package app.domain.services;

import app.domain.models.Loan;
import app.domain.ports.LoanPort;
import app.domain.ports.ClientPort;
import app.domain.enums.approvalFlows.LoanStatus;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class RequestLoan {
    
    private final LoanPort loanPort;
    private final ClientPort clientPort;
    private final RegisterOperation registerOperation;
    
    @Autowired
    public RequestLoan(LoanPort loanPort, ClientPort clientPort, RegisterOperation registerOperation) {
        this.loanPort = loanPort;
        this.clientPort = clientPort;
        this.registerOperation = registerOperation;
    }
    
    public void requestLoan(Loan loan, String clientDocument) throws BusinessException {
        validateLoanData(loan);
        
        if (!clientPort.existsByDocument(clientDocument)) {
            throw new BusinessException("El cliente no existe");
        }
        
        loan.setLoanStatus(LoanStatus.IN_STUDY);
        loanPort.save(loan);
        
        // Registrar solicitud de préstamo en bitácora
        registerOperation.registerLoanRequested(
            clientDocument,
            loan.getLoanId(),
            loan.getAmountRequested()
        );
    }
    
    private void validateLoanData(Loan loan) throws BusinessException {
        if (loan.getLoanType() == null) {
            throw new BusinessException("El tipo de préstamo es obligatorio");
        }
        
        if (loan.getAmountRequested() == null || loan.getAmountRequested().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto solicitado debe ser mayor a cero");
        }
        
        if (loan.getInterestRate() == null || loan.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("La tasa de interés no puede ser negativa");
        }
        
        if (loan.getTerm() == null || loan.getTerm() <= 0) {
            throw new BusinessException("El plazo debe ser mayor a cero meses");
        }
    }
}

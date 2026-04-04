package app.domain.services;

import app.domain.models.Loan;
import app.domain.enums.LoanStatus;
import app.domain.ports.LoanPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindLoan {
    
    private final LoanPort loanPort;
    
    @Autowired
    public FindLoan(LoanPort loanPort) {
        this.loanPort = loanPort;
    }
    
    public Loan findById(Long loanId) throws BusinessException {
        Loan loan = loanPort.findById(loanId);
        if (loan == null) {
            throw new BusinessException("El préstamo no existe");
        }
        return loan;
    }
    
    public List<Loan> findByClientDocument(String clientDocument) {
        return loanPort.findByClientDocument(clientDocument);
    }
    
    public List<Loan> findByStatus(LoanStatus status) {
        return loanPort.findByStatus(status);
    }
    
    public List<Loan> findAll() {
        return loanPort.findAll();
    }
}

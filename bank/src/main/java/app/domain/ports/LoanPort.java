package app.domain.ports;

import java.util.List;
import app.domain.models.Loan;
import app.domain.enums.LoanStatus;

public interface LoanPort {
    
    boolean existsById(Long loanId);
    
    boolean existsByClientDocument(String clientDocument);
    
    void save(Loan loan);
    
    void update(Loan loan);
    
    void deleteById(Long loanId);
    
    Loan findById(Long loanId);
    
    List<Loan> findByClientDocument(String clientDocument);
    
    List<Loan> findByStatus(LoanStatus status);
    
    List<Loan> findAll();
}

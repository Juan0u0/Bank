package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    
    boolean existsByLoanId(Long loanId);
    
    LoanEntity findByLoanId(Long loanId);
    
    List<LoanEntity> findByClientDocument(String clientDocument);
    
    List<LoanEntity> findByLoanStatus(String loanStatus);
    
    void deleteByLoanId(Long loanId);
}

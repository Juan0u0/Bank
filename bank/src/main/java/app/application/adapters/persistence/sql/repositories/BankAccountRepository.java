package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    
    boolean existsByAccountNumber(String accountNumber);
    
    boolean existsByClientDocument(String clientDocument);
    
    BankAccountEntity findByAccountNumber(String accountNumber);
    
    List<BankAccountEntity> findByClientDocument(String clientDocument);
    
    void deleteByAccountNumber(String accountNumber);
}

package app.domain.ports;

import java.util.List;
import java.math.BigDecimal;
import app.domain.models.BankAccount;

public interface BankAccountPort {
    
    boolean existsByAccountNumber(String accountNumber);
    
    boolean existsByClientDocument(String clientDocument);
    
    void save(BankAccount account);
    
    void update(BankAccount account);
    
    void deleteByAccountNumber(String accountNumber);
    
    BankAccount findByAccountNumber(String accountNumber);
    
    List<BankAccount> findByClientDocument(String clientDocument);
    
    List<BankAccount> findAll();
    
    void updateBalance(String accountNumber, BigDecimal newBalance);
}

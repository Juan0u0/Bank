package app.domain.ports;

import java.util.List;
import app.domain.models.BankProduct;

public interface BankProductPort {
    
    boolean existsById(Long productId);
    
    void save(BankProduct product);
    
    void update(BankProduct product);
    
    void deleteById(Long productId);
    
    BankProduct findById(Long productId);
    
    List<BankProduct> findByCategory(String category);
    
    List<BankProduct> findAll();
}

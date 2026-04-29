package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.BankProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankProductRepository extends JpaRepository<BankProductEntity, Long> {
    
    boolean existsByProductId(Long productId);
    
    BankProductEntity findByProductId(Long productId);
    
    List<BankProductEntity> findByCategory(String category);
    
    void deleteByProductId(Long productId);
}

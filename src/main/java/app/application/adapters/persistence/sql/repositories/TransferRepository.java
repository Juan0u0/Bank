package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    
    boolean existsByTransferId(Long transferId);
    
    TransferEntity findByTransferId(Long transferId);
    
    List<TransferEntity> findByOriginAccountNumber(String accountNumber);
    
    List<TransferEntity> findByDestinationAccountNumber(String accountNumber);
    
    List<TransferEntity> findByTransferStatus(String transferStatus);
    
    void deleteByTransferId(Long transferId);
}

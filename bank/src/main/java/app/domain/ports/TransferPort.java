package app.domain.ports;

import java.util.List;
import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;

public interface TransferPort {
    
    boolean existsById(Long transferId);
    
    void save(Transfer transfer);
    
    void update(Transfer transfer);
    
    void deleteById(Long transferId);
    
    Transfer findById(Long transferId);
    
    List<Transfer> findByOriginAccountNumber(String accountNumber);
    
    List<Transfer> findByDestinationAccountNumber(String accountNumber);
    
    List<Transfer> findByStatus(TransferStatus status);
    
    List<Transfer> findAll();
    
    List<Transfer> findExpiredTransfers();
}

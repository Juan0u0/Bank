package app.domain.ports;

import java.util.List;
import app.domain.models.OperationLog;

public interface OperationLogPort {
    
    void save(OperationLog log);
    
    OperationLog findById(Long logId);
    
    List<OperationLog> findByUserDocument(String userDocument);
    
    List<OperationLog> findByOperationType(String operationType);
    
    List<OperationLog> findByAccountNumber(String accountNumber);
    
    List<OperationLog> findByUserRole(String role);
    
    List<OperationLog> findAll();
}

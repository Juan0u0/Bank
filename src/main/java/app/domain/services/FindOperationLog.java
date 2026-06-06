package app.domain.services;

import app.domain.models.OperationLog;
import app.domain.ports.OperationLogPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindOperationLog {
    
    private final OperationLogPort operationLogPort;
    
    @Autowired
    public FindOperationLog(OperationLogPort operationLogPort) {
        this.operationLogPort = operationLogPort;
    }
    
    public OperationLog findById(Long logId) throws BusinessException {
        OperationLog log = operationLogPort.findById(logId);
        if (log == null) {
            throw new BusinessException("El registro de bitácora no existe");
        }
        return log;
    }
    
    public List<OperationLog> findByUserDocument(String userDocument) {
        return operationLogPort.findByUserDocument(userDocument);
    }
    
    public List<OperationLog> findByOperationType(String operationType) {
        return operationLogPort.findByOperationType(operationType);
    }
    
    public List<OperationLog> findByAccountNumber(String accountNumber) {
        return operationLogPort.findByAccountNumber(accountNumber);
    }
    
    public List<OperationLog> findByUserRole(String role) {
        return operationLogPort.findByUserRole(role);
    }
    
    public List<OperationLog> findAll() {
        return operationLogPort.findAll();
    }
}

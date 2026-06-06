package app.application.adapters.persistence.mongodb;

import app.application.adapters.persistence.mongodb.documents.OperationLogDocument;
import app.application.adapters.persistence.mongodb.repositories.OperationLogRepository;
import app.domain.ports.OperationLogPort;
import app.domain.models.OperationLog;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OperationLogPersistenceAdapter implements OperationLogPort {

    private final OperationLogRepository repository;

    public OperationLogPersistenceAdapter(OperationLogRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra un cambio de estado de préstamo en la bitácora
     */
    public void logLoanStatusChange(String userDocument, Long loanId, String previousStatus, 
                                   String newStatus, Map<String, Object> details) {
        OperationLogDocument log = new OperationLogDocument();
        log.setId(UUID.randomUUID().toString());
        log.setUserDocument(userDocument);
        log.setOperationType("LOAN_STATUS_CHANGE");
        log.setOperationDate(LocalDateTime.now());
        log.setEntityType("LOAN");
        log.setEntityId(loanId.toString());
        log.setPreviousState(previousStatus);
        log.setNewState(newStatus);
        log.setDetails(details);
        log.setStatus("COMPLETED");
        
        repository.save(log);
    }

    /**
     * Registra una operación genérica en la bitácora
     */
    public void logOperation(String userDocument, String operationType, String entityType, 
                            String entityId, Map<String, Object> details) {
        OperationLogDocument log = new OperationLogDocument();
        log.setId(UUID.randomUUID().toString());
        log.setUserDocument(userDocument);
        log.setOperationType(operationType);
        log.setOperationDate(LocalDateTime.now());
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        log.setStatus("COMPLETED");
        
        repository.save(log);
    }

    /**
     * Obtiene el historial de operaciones de una entidad
     */
    public java.util.List<OperationLogDocument> getEntityOperationHistory(String entityId) {
        return repository.findByEntityIdOrderByOperationDateDesc(entityId);
    }

    /**
     * Obtiene el historial de operaciones de un usuario
     */
    public java.util.List<OperationLogDocument> getUserOperationHistory(String userDocument) {
        return repository.findByUserDocument(userDocument);
    }

    // ========== Implementación de OperationLogPort ==========

    @Override
    public void save(OperationLog log) {
        OperationLogDocument document = new OperationLogDocument();
        document.setId(UUID.randomUUID().toString());
        document.setUserDocument(log.getUser() != null ? log.getUser().getDocument() : "UNKNOWN");
        document.setOperationType(log.getOperationType());
        document.setOperationDate(log.getOperationDate() != null ? log.getOperationDate() : LocalDateTime.now());
        document.setEntityType("GENERIC");
        document.setEntityId(log.getLogId() != null ? log.getLogId().toString() : UUID.randomUUID().toString());
        document.setDetails(log.getDetails() != null ? log.getDetails() : new java.util.HashMap<>());
        document.setStatus("COMPLETED");
        
        repository.save(document);
    }

    @Override
    public OperationLog findById(Long logId) {
        List<OperationLogDocument> documents = repository.findAll();
        OperationLogDocument document = documents.stream()
            .filter(doc -> logId.toString().equals(doc.getEntityId()))
            .findFirst()
            .orElse(null);
        
        return document != null ? mapDocumentToModel(document) : null;
    }

    @Override
    public List<OperationLog> findByUserDocument(String userDocument) {
        return repository.findByUserDocument(userDocument).stream()
            .map(this::mapDocumentToModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<OperationLog> findByOperationType(String operationType) {
        return repository.findByOperationType(operationType).stream()
            .map(this::mapDocumentToModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<OperationLog> findByAccountNumber(String accountNumber) {
        return repository.findAll().stream()
            .filter(doc -> doc.getDetails() != null && accountNumber.equals(doc.getDetails().get("accountNumber")))
            .map(this::mapDocumentToModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<OperationLog> findByUserRole(String role) {
        return repository.findAll().stream()
            .filter(doc -> doc.getDetails() != null && role.equals(doc.getDetails().get("userRole")))
            .map(this::mapDocumentToModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<OperationLog> findAll() {
        return repository.findAll().stream()
            .map(this::mapDocumentToModel)
            .collect(Collectors.toList());
    }

    /**
     * Mapea OperationLogDocument a OperationLog
     */
    private OperationLog mapDocumentToModel(OperationLogDocument document) {
        OperationLog log = new OperationLog();
        log.setOperationType(document.getOperationType());
        log.setOperationDate(document.getOperationDate());
        log.setDetails(document.getDetails() != null ? document.getDetails() : new java.util.HashMap<>());
        
        return log;
    }
}

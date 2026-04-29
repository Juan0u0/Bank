package app.application.adapters.persistence.mongodb.repositories;

import app.application.adapters.persistence.mongodb.documents.OperationLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends MongoRepository<OperationLogDocument, String> {
    
    List<OperationLogDocument> findByUserDocument(String userDocument);
    
    List<OperationLogDocument> findByEntityId(String entityId);
    
    List<OperationLogDocument> findByOperationType(String operationType);
    
    List<OperationLogDocument> findByEntityType(String entityType);
    
    List<OperationLogDocument> findByOperationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<OperationLogDocument> findByUserDocumentAndOperationDateBetween(String userDocument, LocalDateTime startDate, LocalDateTime endDate);
    
    List<OperationLogDocument> findByEntityIdOrderByOperationDateDesc(String entityId);
}

package app.application.adapters.persistence.mongodb.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "operation_logs")
public class OperationLogDocument {

    @Id
    private String id;

    @Indexed
    private String userDocument;

    private String operationType;

    @Indexed
    private LocalDateTime operationDate;

    private String entityType;

    private String entityId;

    private Map<String, Object> details;

    private String status;

    private String previousState;

    private String newState;
}

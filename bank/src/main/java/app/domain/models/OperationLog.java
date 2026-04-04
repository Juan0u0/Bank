package app.domain.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import app.domain.enums.SistemRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class OperationLog {
    private Long logId;
    private String operationType;
    private LocalDateTime operationDate;
    private User user; //Usuario que realizó la operación
    private SistemRole userRole; //Rol del usuario en el momento de la operación
    private BankAccount bankAccount; //Cuenta bancaria involucrada en la operación (si aplica)
    private Map<String, Object> details = new HashMap<>(); //Detalles adicionales flexibles sobre la operación
}

package app.domain.services;

import app.domain.models.Transfer;
import app.domain.models.BankAccount;
import app.domain.enums.AccountStatus;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ExecuteTransfer {
    
    private final BankAccountPort accountPort;
    private final RegisterOperation registerOperation;
    
    @Autowired
    public ExecuteTransfer(BankAccountPort accountPort, RegisterOperation registerOperation) {
        this.accountPort = accountPort;
        this.registerOperation = registerOperation;
    }
    
    /**
     * Ejecuta la transferencia entre dos cuentas y registra la auditoría con saldos antes y después
     */
    public void executeTransfer(Transfer transfer, String userDocument) throws BusinessException {
        BankAccount originAccount = transfer.getOriginAccount();
        BankAccount destAccount = transfer.getDestinationAccount();
        
        if (originAccount == null || destAccount == null) {
            throw new BusinessException("Las cuentas de origen o destino son nulas");
        }
        
        if (originAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("La cuenta origen no está activa");
        }
        
        if (destAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("La cuenta destino no está activa");
        }
        
        if (originAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new BusinessException("Saldo insuficiente en la cuenta origen");
        }
        
        if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto de transferencia debe ser mayor a cero");
        }
        
        // Capturar saldos ANTES de la operación
        BigDecimal balanceBeforeOrigin = originAccount.getBalance();
        BigDecimal balanceBeforeDestination = destAccount.getBalance();
        
        // Actualizar saldo cuenta origen
        BigDecimal newOriginBalance = balanceBeforeOrigin.subtract(transfer.getAmount());
        originAccount.setBalance(newOriginBalance);
        accountPort.update(originAccount);
        
        // Actualizar saldo cuenta destino
        BigDecimal newDestBalance = balanceBeforeDestination.add(transfer.getAmount());
        destAccount.setBalance(newDestBalance);
        accountPort.update(destAccount);
        
        // Registrar en bitácora con auditoría completa
        registerOperation.registerTransferExecuted(
            userDocument,
            originAccount.getAccountNumber(),
            balanceBeforeOrigin,
            newOriginBalance,
            destAccount.getAccountNumber(),
            balanceBeforeDestination,
            newDestBalance,
            transfer.getAmount()
        );
    }
}

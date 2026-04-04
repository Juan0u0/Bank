package app.domain.services;

import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import app.domain.enums.SistemRole;
import app.domain.ports.TransferPort;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class ApproveTransfer {
    
    private final TransferPort transferPort;
    private final BankAccountPort accountPort;
    private final RegisterOperation registerOperation;
    
    @Autowired
    public ApproveTransfer(TransferPort transferPort, BankAccountPort accountPort, RegisterOperation registerOperation) {
        this.transferPort = transferPort;
        this.accountPort = accountPort;
        this.registerOperation = registerOperation;
    }
    
    public void approveTransfer(Long transferId, SistemRole userRole) throws BusinessException {
        if (userRole != SistemRole.COMPANY_SUPERVISOR) {
            throw new BusinessException("Solo un Supervisor de Empresa puede aprobar transferencias");
        }
        
        Transfer transfer = transferPort.findById(transferId);
        if (transfer == null) {
            throw new BusinessException("La transferencia no existe");
        }
        
        if (transfer.getTransferStatus() != TransferStatus.AWAITING_APPROVAL) {
            throw new BusinessException("La transferencia solo puede ser aprobada si está en estado 'En espera de aprobación'");
        }
        
        var originAccount = transfer.getOriginAccount();
        if (originAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new BusinessException("Saldo insuficiente para ejecutar la transferencia");
        }
        
        executeTransfer(transfer);
        transfer.setTransferStatus(TransferStatus.EXECUTED);
        transfer.setApprovalDate(LocalDateTime.now());
        
        transferPort.update(transfer);
    }
    
    /**
     * Ejecuta la transferencia y registra el evento en la bitácora con auditoría 
     * de saldos "antes y después" según especificación.
     */
    private void executeTransfer(Transfer transfer) throws BusinessException {
        var originAccount = transfer.getOriginAccount();
        var destAccount = transfer.getDestinationAccount();
        
        // AUDITORÍA: Capturar saldos ANTES de la operación
        BigDecimal balanceBeforeOrigin = originAccount.getBalance();
        BigDecimal balanceBeforeDestination = destAccount.getBalance();
        
        // Actualizar saldo cuenta origen
        BigDecimal newOriginBalance = originAccount.getBalance().subtract(transfer.getAmount());
        accountPort.updateBalance(originAccount.getAccountNumber(), newOriginBalance);
        
        // Actualizar saldo cuenta destino
        BigDecimal newDestBalance = destAccount.getBalance().add(transfer.getAmount());
        accountPort.updateBalance(destAccount.getAccountNumber(), newDestBalance);
        
        // AUDITORÍA: Registrar en Bitácora con saldos DESPUÉS
        String userDocument = transfer.getApprovedBy() != null 
            ? transfer.getApprovedBy().getDocument() 
            : "SYSTEM";
        
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

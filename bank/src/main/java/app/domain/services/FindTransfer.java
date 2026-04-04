package app.domain.services;

import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import app.domain.ports.TransferPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class FindTransfer {
    
    private final TransferPort transferPort;
    private final RegisterOperation registerOperation;
    private static final Long APPROVAL_TIMEOUT_MINUTES = 60L;
    
    @Autowired
    public FindTransfer(TransferPort transferPort, RegisterOperation registerOperation) {
        this.transferPort = transferPort;
        this.registerOperation = registerOperation;
    }
    
    public Transfer findById(Long transferId) throws BusinessException {
        Transfer transfer = transferPort.findById(transferId);
        if (transfer == null) {
            throw new BusinessException("La transferencia no existe");
        }
        return transfer;
    }
    
    public List<Transfer> findByOriginAccountNumber(String accountNumber) {
        return transferPort.findByOriginAccountNumber(accountNumber);
    }
    
    public List<Transfer> findByDestinationAccountNumber(String accountNumber) {
        return transferPort.findByDestinationAccountNumber(accountNumber);
    }
    
    public List<Transfer> findByStatus(TransferStatus status) {
        return transferPort.findByStatus(status);
    }
    
    public List<Transfer> findAll() {
        return transferPort.findAll();
    }
    
    public void expireOldTransfers() throws BusinessException {
        List<Transfer> expiredTransfers = transferPort.findExpiredTransfers();
        
        for (Transfer transfer : expiredTransfers) {
            if (transfer.getTransferStatus() == TransferStatus.AWAITING_APPROVAL) {
                LocalDateTime creationDateTime = transfer.getCreationDate();
                LocalDateTime now = LocalDateTime.now();
                
                long minutesElapsed = ChronoUnit.MINUTES.between(creationDateTime, now);
                
                if (minutesElapsed > APPROVAL_TIMEOUT_MINUTES) {
                    // Cambiar estado a EXPIRED
                    transfer.setTransferStatus(TransferStatus.EXPIRED);
                    transferPort.update(transfer);
                    
                    // Registrar en bitácora - NO mueve dinero, solo registra expiración
                    registerOperation.registerTransferExpired(
                        transfer.getTransferId().toString(),
                        transfer.getOriginAccount().getAccountNumber(),
                        transfer.getDestinationAccount().getAccountNumber(),
                        transfer.getAmount()
                    );
                }
            }
        }
    }
}
    


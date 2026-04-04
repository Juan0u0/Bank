package app.domain.services;

import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import app.domain.enums.SistemRole;
import app.domain.ports.TransferPort;
import app.domain.ports.BankAccountPort;
import app.domain.ports.UserPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class CreateTransfer {
    
    private final TransferPort transferPort;
    private final BankAccountPort accountPort;
    private final UserPort userPort;
    private static final BigDecimal TRANSFER_THRESHOLD = new BigDecimal("100000.00");
    
    @Autowired
    public CreateTransfer(TransferPort transferPort, BankAccountPort accountPort, UserPort userPort) {
        this.transferPort = transferPort;
        this.accountPort = accountPort;
        this.userPort = userPort;
    }
    
    public void createTransfer(Transfer transfer, String originAccountNumber, String destinationAccountNumber,
                               String creatorDocument) throws BusinessException {
        validateTransferData(transfer);
        
        var originAccount = accountPort.findByAccountNumber(originAccountNumber);
        if (originAccount == null) {
            throw new BusinessException("La cuenta origen no existe");
        }
        
        var destAccount = accountPort.findByAccountNumber(destinationAccountNumber);
        if (destAccount == null) {
            throw new BusinessException("La cuenta destino no existe");
        }
        
        var creator = userPort.findByDocument(creatorDocument);
        if (creator == null) {
            throw new BusinessException("Usuario no encontrado");
        }
        
        if (originAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new BusinessException("Saldo insuficiente para realizar la transferencia");
        }
        
        transfer.setOriginAccount(originAccount);
        transfer.setDestinationAccount(destAccount);
        transfer.setCreatedBy(creator);
        transfer.setCreationDate(LocalDateTime.now());
        
        // Determinar estado inicial
        if (creator.getRole() == SistemRole.COMPANY_EMPLOYEE && transfer.getAmount().compareTo(TRANSFER_THRESHOLD) > 0) {
            transfer.setTransferStatus(TransferStatus.AWAITING_APPROVAL);
        } else {
            transfer.setTransferStatus(TransferStatus.EXECUTED);
            executeTransfer(transfer);
        }
        
        transferPort.save(transfer);
    }
    
    private void executeTransfer(Transfer transfer) throws BusinessException {
        var originAccount = transfer.getOriginAccount();
        var destAccount = transfer.getDestinationAccount();
        
        // Actualizar saldo cuenta origen
        BigDecimal newOriginBalance = originAccount.getBalance().subtract(transfer.getAmount());
        accountPort.updateBalance(originAccount.getAccountNumber(), newOriginBalance);
        
        // Actualizar saldo cuenta destino
        BigDecimal newDestBalance = destAccount.getBalance().add(transfer.getAmount());
        accountPort.updateBalance(destAccount.getAccountNumber(), newDestBalance);
    }
    
    private void validateTransferData(Transfer transfer) throws BusinessException {
        if (transfer.getAmount() == null || transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto a transferir debe ser mayor a cero");
        }
    }
}

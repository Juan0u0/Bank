package app.domain.services;

import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import app.domain.enums.SistemRole;
import app.domain.ports.TransferPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RejectTransfer {
    
    private final TransferPort transferPort;
    
    @Autowired
    public RejectTransfer(TransferPort transferPort) {
        this.transferPort = transferPort;
    }
    
    public void rejectTransfer(Long transferId, SistemRole userRole) throws BusinessException {
        if (userRole != SistemRole.COMPANY_SUPERVISOR) {
            throw new BusinessException("Solo un Supervisor de Empresa puede rechazar transferencias");
        }
        
        Transfer transfer = transferPort.findById(transferId);
        if (transfer == null) {
            throw new BusinessException("La transferencia no existe");
        }
        
        if (transfer.getTransferStatus() != TransferStatus.AWAITING_APPROVAL) {
            throw new BusinessException("La transferencia solo puede ser rechazada si está en estado 'En espera de aprobación'");
        }
        
        transfer.setTransferStatus(TransferStatus.REJECTED);
        transferPort.update(transfer);
    }
}

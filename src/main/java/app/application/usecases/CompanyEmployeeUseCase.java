package app.application.usecases;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.exceptions.BusinessException;
import app.domain.models.Transfer;
import app.domain.services.CreateTransfer;
import app.domain.services.FindAccount;
import app.domain.services.FindTransfer;
import app.application.adapters.api.request.TransferRequest;

@Service
public class CompanyEmployeeUseCase {
    
    @Autowired
    private CreateTransfer createTransfer;
    @Autowired
    private FindAccount findAccount;
    @Autowired
    private FindTransfer findTransfer;

    public CompanyEmployeeUseCase(CreateTransfer createTransfer, FindAccount findAccount, FindTransfer findTransfer) {
        this.createTransfer = createTransfer;
        this.findAccount = findAccount;
        this.findTransfer = findTransfer;
    }
    public void createTransfer(Transfer transfer, String originAccountNumber, String destinationAccountNumber,
                               String creatorDocument) throws BusinessException {
        createTransfer.createTransfer(transfer, originAccountNumber, destinationAccountNumber, creatorDocument);
    }
    public void findAccount(String clientDocument) throws BusinessException {
        findAccount.findByClientDocument(clientDocument);
    }
    public void findTransfer(Long transferId) throws BusinessException {
        findTransfer.findById(transferId);
    }

    public List<Transfer> processBulkTransfers(String userDocument, List<TransferRequest> transfers)
            throws BusinessException {
        List<Transfer> createdTransfers = new ArrayList<>();

        for (TransferRequest transferRequest : transfers) {
            Transfer transfer = new Transfer();
            transfer.setAmount(transferRequest.getAmount());
            createTransfer.createTransfer(
                transfer,
                transferRequest.getOriginAccountNumber(),
                transferRequest.getDestinationAccountNumber(),
                userDocument
            );
            createdTransfers.add(transfer);
        }

        return createdTransfers;
    }

}

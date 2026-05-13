package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.services.CreateTransfer;
import app.domain.services.FindAccount;
import app.domain.services.FindLoan;
import app.domain.services.FindTransfer;
import app.domain.services.FindOperationLog;
import app.domain.services.RequestLoan;

import java.util.List;

@Service
public class NaturalClientUseCase {

    @Autowired
    private FindAccount findAccount;
    @Autowired
    private FindLoan findLoan;
    @Autowired
    private FindTransfer findTransfer;
    @Autowired
    private RequestLoan requestLoan;
    @Autowired
    private CreateTransfer createTransfer;
    @Autowired
    private FindOperationLog findOperationLog;

    public NaturalClientUseCase(   FindAccount findAccount, FindLoan findLoan, FindTransfer findTransfer, 
                            RequestLoan requestLoan, CreateTransfer createTransfer, FindOperationLog findOperationLog) {

        this.findAccount = findAccount;
        this.findLoan = findLoan;
        this.findTransfer = findTransfer;
        this.requestLoan = requestLoan;
        this.createTransfer = createTransfer;
        this.findOperationLog = findOperationLog;
    }
    
    public void FindAccount(String accountNumber) throws BusinessException {
        findAccount.findByAccountNumber(accountNumber);
    }
    public void FindLoan(Long loanId) throws BusinessException {
        findLoan.findById(loanId);
    }
    public void FindTransfer(Long transferId) throws BusinessException {
        findTransfer.findById(transferId);
    }
    public void RequestLoan (Loan loan, String clientDocument) throws BusinessException {
        requestLoan.requestLoan(loan, clientDocument);
    }
    public void CreateTransfer (Transfer transfer, String originAccountNumber, String destinationAccountNumber,
                                String creatorDocument) throws BusinessException {
        createTransfer.createTransfer(transfer, originAccountNumber, destinationAccountNumber, creatorDocument);
    }
    
    public List<app.domain.models.BankAccount> getClientAccounts(String clientDocument) throws BusinessException {
        return findAccount.findByClientDocument(clientDocument);
    }
    
    public List<Loan> getClientLoans(String clientDocument) throws BusinessException {
        return findLoan.findByClientDocument(clientDocument);
    }
    
    public String getPersonalAuditLog(String clientDocument) throws BusinessException {
        var logs = findOperationLog.findByUserDocument(clientDocument);
        return logs != null ? logs.toString() : "No hay registros de operaciones";
    }
}

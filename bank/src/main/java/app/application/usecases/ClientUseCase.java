package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.models.Client;
import app.domain.models.Loan;
import app.domain.services.CreateTransfer;
import app.domain.services.FindAccount;
import app.domain.services.FindLoan;
import app.domain.services.FindTransfer;
import app.domain.services.RequestLoan;

@Service
public class ClientUseCase {

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

    public ClientUseCase(   FindAccount findAccount, FindLoan findLoan, FindTransfer findTransfer, 
                            RequestLoan requestLoan, CreateTransfer createTransfer) {

        this.findAccount = findAccount;
        this.findLoan = findLoan;
        this.findTransfer = findTransfer;
        this.requestLoan = requestLoan;
        this.createTransfer = createTransfer;
    }
    
    public void FindAccount(String accountNumber) {
        findAccount.findByAccountNumber(accountNumber);
    }
    public void FindLoan(String loanId) {
        findLoan.findById(Long.parseLong(loanId));
    }
    public void FindTransfer(String transferId) {
        findTransfer.findById(Long.parseLong(transferId));
    }
    public void RequestLoan (RequestLoan requestLoan) {
        requestLoan.requestLoan(Loan loan, String clientDocument);
    }
    
}

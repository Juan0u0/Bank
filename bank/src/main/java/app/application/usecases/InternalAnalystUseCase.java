package app.application.usecases;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.enums.SistemRole;
import app.domain.exceptions.BusinessException;
import app.domain.models.BankAccount;
import app.domain.services.ApproveLoan;
import app.domain.services.DisburseLoan;
import app.domain.services.FindOperationLog;
import app.domain.services.ManageAccount;
import app.domain.services.RejectLoan;

@Service
public class InternalAnalystUseCase {
    
    @Autowired
    private ApproveLoan approveLoan; 
    @Autowired
    private RejectLoan rejectLoan; 
    @Autowired
    private DisburseLoan disburseLoan; 
    @Autowired
    private FindOperationLog findOperationLog;
    @Autowired
    private ManageAccount manageAccount; 
    
    public InternalAnalystUseCase(   ApproveLoan approveLoan, RejectLoan rejectLoan, DisburseLoan disburseLoan,
                                    FindOperationLog findOperationLog, ManageAccount manageAccount) {
        this.approveLoan = approveLoan;
        this.rejectLoan = rejectLoan;
        this.disburseLoan = disburseLoan;
        this.findOperationLog = findOperationLog;
        this.manageAccount = manageAccount;
    }
    public void approveLoan (Long loanId, BigDecimal amountApproved, SistemRole userRole) throws BusinessException {
        approveLoan.approveLoan(loanId, amountApproved, userRole);
    }
    public void rejectLoan (Long loanId, SistemRole userRole) throws BusinessException {
        rejectLoan.rejectLoan(loanId, userRole);
    }
    public void disburseLoan (Long loanId, SistemRole userRole) throws BusinessException {
        disburseLoan.disburse(loanId, userRole);
    }
    public void findOperationLog (Long logId) throws BusinessException {
        findOperationLog.findById(logId);
    }
    public void blockAccount (BankAccount account){
         manageAccount.manageAccount( account,  "block");
    }
    public void unblockAccount (BankAccount account){
        manageAccount.manageAccount(account, "unblock");
    }
    public void closeAccount (BankAccount account){
        manageAccount.manageAccount(account, "close");
    }
}

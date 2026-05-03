package app.application.usecases;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.exceptions.BusinessException;
import app.domain.models.BankAccount;
import app.domain.models.User;
import app.domain.services.CreateAccount;
import app.domain.services.CreateUser;
import app.domain.services.DepositMoney;
import app.domain.services.FindAccount;
import app.domain.services.WithdrawMoney;

@Service
public class WindowEmployeeUseCase {

    @Autowired
    private CreateAccount createAccount;
    @Autowired
    private CreateUser createUser;
    @Autowired
    private DepositMoney depositMoney;
    @Autowired
    private WithdrawMoney withdrawMoney;
    @Autowired
    private FindAccount findAccount;

    public WindowEmployeeUseCase(   CreateAccount createAccount, CreateUser createUser, DepositMoney depositMoney, WithdrawMoney withdrawMoney,
                                    FindAccount findAccount) {
        this.createAccount = createAccount;
        this.createUser = createUser;
        this.depositMoney = depositMoney;
        this.withdrawMoney = withdrawMoney;
        this.findAccount = findAccount;
    }

    public void registerUser(User user) throws BusinessException {
        createUser.createUser(user);
    }

    public void createAccount (BankAccount account, String clientDocument) throws BusinessException {
        createAccount.createAccount(account, clientDocument);
    }
    public void depositMoney (String accountNumber,BigDecimal amount, String userDocument) throws BusinessException {
        depositMoney.deposit(accountNumber, amount, userDocument);
    }
    public void withdrawMoney (String accountNumber, BigDecimal amount, String userDocument) throws BusinessException {
        withdrawMoney.withdraw(accountNumber, amount, userDocument);
    }
    public void findAccount(String accountNumber) throws BusinessException {
        findAccount.findByAccountNumber(accountNumber);
    }
    
    public BankAccount getAccount(String accountNumber) throws BusinessException {
        return findAccount.findByAccountNumber(accountNumber);
    }
}

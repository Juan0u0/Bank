package app.domain.services;

import app.domain.models.BankAccount;
import app.domain.enums.AccountStatus;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class DepositMoney {
    
    private final BankAccountPort accountPort;
    private final RegisterOperation registerOperation;
    
    @Autowired
    public DepositMoney(BankAccountPort accountPort, RegisterOperation registerOperation) {
        this.accountPort = accountPort;
        this.registerOperation = registerOperation;
    }
    
    public void deposit(String accountNumber, BigDecimal amount, String userDocument) throws BusinessException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("La cantidad a depositar debe ser mayor a cero");
        }
        
        BankAccount account = accountPort.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("La cuenta no existe");
        }
        
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("La cuenta no está activa. No se puede realizar depósito");
        }
        
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal newBalance = balanceBefore.add(amount);
        
        account.setBalance(newBalance);
        accountPort.update(account);
        
        registerOperation.registerDeposit(userDocument, accountNumber, balanceBefore, newBalance, amount);
    }
}

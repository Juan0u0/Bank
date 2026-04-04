package app.domain.services;

import app.domain.enums.AccountStatus;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ManageAccount {
    
    private final BankAccountPort accountPort;
    
    @Autowired
    public ManageAccount(BankAccountPort accountPort) {
        this.accountPort = accountPort;
    }
    
    public void blockAccount(String accountNumber) throws BusinessException {
        var account = accountPort.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("La cuenta no existe");
        }
        account.setAccountStatus(AccountStatus.BLOCKED);
        accountPort.update(account);
    }
    
    public void unblockAccount(String accountNumber) throws BusinessException {
        var account = accountPort.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("La cuenta no existe");
        }
        account.setAccountStatus(AccountStatus.ACTIVE);
        accountPort.update(account);
    }
    
    public void closeAccount(String accountNumber) throws BusinessException {
        var account = accountPort.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("La cuenta no existe");
        }
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("No se puede cerrar una cuenta con saldo disponible");
        }
        account.setAccountStatus(AccountStatus.CLOSED);
        accountPort.update(account);
    }
}

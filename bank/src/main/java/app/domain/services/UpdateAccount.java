package app.domain.services;

import app.domain.models.BankAccount;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccount {
    
    private final BankAccountPort accountPort;
    
    @Autowired
    public UpdateAccount(BankAccountPort accountPort) {
        this.accountPort = accountPort;
    }
    
    public void updateAccount(BankAccount account) throws BusinessException {
        if (!accountPort.existsByAccountNumber(account.getAccountNumber())) {
            throw new BusinessException("La cuenta no existe");
        }
        
        accountPort.update(account);
    }
}

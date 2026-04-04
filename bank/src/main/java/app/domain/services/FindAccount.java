package app.domain.services;

import app.domain.models.BankAccount;
import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindAccount {
    
    private final BankAccountPort accountPort;
    
    @Autowired
    public FindAccount(BankAccountPort accountPort) {
        this.accountPort = accountPort;
    }
    
    public BankAccount findByAccountNumber(String accountNumber) throws BusinessException {
        BankAccount account = accountPort.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("La cuenta no existe");
        }
        return account;
    }
    
    public List<BankAccount> findByClientDocument(String clientDocument) {
        return accountPort.findByClientDocument(clientDocument);
    }
    
    public List<BankAccount> findAll() {
        return accountPort.findAll();
    }
}

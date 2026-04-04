package app.domain.services;

import app.domain.ports.BankAccountPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteAccount {
    
    private final BankAccountPort accountPort;
    
    @Autowired
    public DeleteAccount(BankAccountPort accountPort) {
        this.accountPort = accountPort;
    }
    
    public void deleteAccount(String accountNumber) throws BusinessException {
        if (!accountPort.existsByAccountNumber(accountNumber)) {
            throw new BusinessException("La cuenta no existe");
        }
        accountPort.deleteByAccountNumber(accountNumber);
    }
}

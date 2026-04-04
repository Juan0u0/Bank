package app.domain.services;

import app.domain.models.BankAccount;
import app.domain.enums.AccountStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.ClientPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class CreateAccount {
    
    private final BankAccountPort accountPort;
    private final ClientPort clientPort;
    
    @Autowired
    public CreateAccount(BankAccountPort accountPort, ClientPort clientPort) {
        this.accountPort = accountPort;
        this.clientPort = clientPort;
    }
    
    public void createAccount(BankAccount account, String clientDocument) throws BusinessException {
        validateAccountData(account);
        
        if (!clientPort.existsByDocument(clientDocument)) {
            throw new BusinessException("El cliente no existe");
        }
        
        if (accountPort.existsByAccountNumber(account.getAccountNumber())) {
            throw new BusinessException("Ya existe una cuenta con ese número");
        }
        
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDateTime.now());
        account.setBalance(BigDecimal.ZERO);
        
        accountPort.save(account);
    }
    
    private void validateAccountData(BankAccount account) throws BusinessException {
        if (account.getAccountNumber() == null || account.getAccountNumber().isBlank()) {
            throw new BusinessException("El número de cuenta es obligatorio");
        }
        
        if (account.getAccountType() == null) {
            throw new BusinessException("El tipo de cuenta es obligatorio");
        }
        
        if (account.getCurrency() == null) {
            throw new BusinessException("La moneda es obligatoria");
        }
    }
}

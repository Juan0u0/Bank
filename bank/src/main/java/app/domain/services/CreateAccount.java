package app.domain.services;

import app.domain.models.BankAccount;
import app.domain.ports.BankAccountPort;
import app.domain.ports.ClientPort;
import app.domain.enums.status.AccountStatus;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Random;

@Service
public class CreateAccount {
    
    private final BankAccountPort accountPort;
    private final ClientPort clientPort;
    private final RegisterOperation registerOperation;
    private final Random random = new Random();
    
    @Autowired
    public CreateAccount(BankAccountPort accountPort, ClientPort clientPort, RegisterOperation registerOperation) {
        this.accountPort = accountPort;
        this.clientPort = clientPort;
        this.registerOperation = registerOperation;
    }
    
    public void createAccount(BankAccount account, String clientDocument) throws BusinessException {
        // Generamos el número único antes de las validaciones
        account.setAccountNumber(generateUniqueAccountNumber());
        
        validateAccountData(account);
        
        if (!clientPort.existsByDocument(clientDocument)) {
            throw new BusinessException("El cliente no existe");
        }
        
        // Esta validación se mantiene por seguridad, aunque el generador ya la hace
        if (accountPort.existsByAccountNumber(account.getAccountNumber())) {
            throw new BusinessException("Ya existe una cuenta con ese número");
        }
        
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDateTime.now());
        account.setBalance(BigDecimal.ZERO);
        
        accountPort.save(account);
        
        // Registrar apertura de cuenta en bitácora
        registerOperation.registerAccountOpened(
            clientDocument,
            account.getAccountNumber(),
            account.getAccountType().toString(),
            account.getCurrency().toString()
        );
    }

    /**
     * Genera un número de cuenta aleatorio y verifica en el puerto 
     * que no exista previamente en la base de datos.
     */
    private String generateUniqueAccountNumber() {
        String number;
        boolean exists;
        do {
            // Ejemplo: Genera un número de 10 dígitos
            long rawNumber = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
            number = String.valueOf(rawNumber);
            exists = accountPort.existsByAccountNumber(number);
        } while (exists);
        
        return number;
    }
    
    private void validateAccountData(BankAccount account) throws BusinessException {
        // Se mantiene la validación pero ahora valida el número generado automáticamente
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
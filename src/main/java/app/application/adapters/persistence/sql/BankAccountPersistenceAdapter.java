package app.application.adapters.persistence.sql;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import app.application.adapters.persistence.sql.repositories.BankAccountRepository;
import app.domain.models.BankAccount;
import app.domain.enums.status.AccountStatus;
import app.domain.enums.status.AccountType;
import app.domain.enums.status.Currency;
import app.domain.ports.BankAccountPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountPersistenceAdapter implements BankAccountPort {

    private final BankAccountRepository repository;

    public BankAccountPersistenceAdapter(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    @Override
    public boolean existsByClientDocument(String clientDocument) {
        return repository.existsByClientDocument(clientDocument);
    }

    @Override
    public void save(BankAccount account) {
        repository.save(toEntity(account));
    }

    @Override
    public void update(BankAccount account) {
        BankAccountEntity existing = repository.findByAccountNumber(account.getAccountNumber());
        if (existing != null) {
            existing.setAccountType(account.getAccountType() != null ? account.getAccountType().name() : null);
            existing.setBalance(account.getBalance());
            existing.setCurrency(account.getCurrency() != null ? account.getCurrency().name() : null);
            existing.setAccountStatus(account.getAccountStatus() != null ? account.getAccountStatus().name() : null);
            repository.save(existing);
        }
    }

    @Override
    @Transactional
    public void deleteByAccountNumber(String accountNumber) {
        repository.deleteByAccountNumber(accountNumber);
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return toModel(repository.findByAccountNumber(accountNumber));
    }

    @Override
    public List<BankAccount> findByClientDocument(String clientDocument) {
        return repository.findByClientDocument(clientDocument)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccount findFirstActiveByClientDocument(String clientDocument) {
        List<BankAccountEntity> accounts = repository.findByClientDocument(clientDocument);
        return accounts.stream()
                .filter(acc -> "ACTIVE".equals(acc.getAccountStatus()))
                .map(this::toModel)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BankAccount> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public void updateBalance(String accountNumber, BigDecimal newBalance) {
        BankAccountEntity existing = repository.findByAccountNumber(accountNumber);
        if (existing != null) {
            existing.setBalance(newBalance);
            repository.save(existing);
        }
    }

    private BankAccountEntity toEntity(BankAccount account) {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setAccountNumber(account.getAccountNumber());
        entity.setAccountType(account.getAccountType() != null ? account.getAccountType().name() : null);
        entity.setClientDocument(account.getClient() != null ? account.getClient().getDocument() : null);
        entity.setBalance(account.getBalance());
        entity.setCurrency(account.getCurrency() != null ? account.getCurrency().name() : null);
        entity.setAccountStatus(account.getAccountStatus() != null ? account.getAccountStatus().name() : null);
        entity.setOpeningDate(account.getOpeningDate());
        return entity;
    }

    private BankAccount toModel(BankAccountEntity entity) {
        if (entity == null) return null;
        BankAccount account = new BankAccount();
        account.setAccountNumber(entity.getAccountNumber());
        account.setAccountType(entity.getAccountType() != null ? AccountType.valueOf(entity.getAccountType()) : null);
        account.setBalance(entity.getBalance());
        account.setCurrency(entity.getCurrency() != null ? Currency.valueOf(entity.getCurrency()) : null);
        account.setAccountStatus(entity.getAccountStatus() != null ? AccountStatus.valueOf(entity.getAccountStatus()) : null);
        account.setOpeningDate(entity.getOpeningDate());
        return account;
    }
}

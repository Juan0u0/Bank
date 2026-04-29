package app.application.adapters.persistence.sql;

import app.application.adapters.persistence.sql.entities.TransferEntity;
import app.application.adapters.persistence.sql.repositories.TransferRepository;
import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import app.domain.ports.TransferPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferPersistenceAdapter implements TransferPort {

    private final TransferRepository repository;

    public TransferPersistenceAdapter(TransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsById(Long transferId) {
        return repository.existsByTransferId(transferId);
    }

    @Override
    public void save(Transfer transfer) {
        repository.save(toEntity(transfer));
    }

    @Override
    public void update(Transfer transfer) {
        TransferEntity existing = repository.findByTransferId(transfer.getTransferId());
        if (existing != null) {
            existing.setAmount(transfer.getAmount());
            existing.setTransferStatus(transfer.getTransferStatus() != null ? transfer.getTransferStatus().name() : null);
            existing.setApprovalDate(transfer.getApprovalDate());
            existing.setApprovedByDocument(transfer.getApprovedBy() != null ? transfer.getApprovedBy().getDocument() : null);
            repository.save(existing);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long transferId) {
        repository.deleteByTransferId(transferId);
    }

    @Override
    public Transfer findById(Long transferId) {
        return toModel(repository.findByTransferId(transferId));
    }

    @Override
    public List<Transfer> findByOriginAccountNumber(String accountNumber) {
        return repository.findByOriginAccountNumber(accountNumber)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByDestinationAccountNumber(String accountNumber) {
        return repository.findByDestinationAccountNumber(accountNumber)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByStatus(TransferStatus status) {
        return repository.findByTransferStatus(status.name())
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findExpiredTransfers() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return repository.findByTransferStatus(TransferStatus.PENDING.name())
                .stream()
                .filter(t -> t.getCreationDate().isBefore(oneHourAgo))
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private TransferEntity toEntity(Transfer transfer) {
        TransferEntity entity = new TransferEntity();
        entity.setTransferId(transfer.getTransferId());
        entity.setOriginAccountNumber(transfer.getOriginAccount() != null ? transfer.getOriginAccount().getAccountNumber() : null);
        entity.setDestinationAccountNumber(transfer.getDestinationAccount() != null ? transfer.getDestinationAccount().getAccountNumber() : null);
        entity.setAmount(transfer.getAmount());
        entity.setCreationDate(transfer.getCreationDate());
        entity.setApprovalDate(transfer.getApprovalDate());
        entity.setTransferStatus(transfer.getTransferStatus() != null ? transfer.getTransferStatus().name() : null);
        entity.setCreatedByDocument(transfer.getCreatedBy() != null ? transfer.getCreatedBy().getDocument() : null);
        entity.setApprovedByDocument(transfer.getApprovedBy() != null ? transfer.getApprovedBy().getDocument() : null);
        return entity;
    }

    private Transfer toModel(TransferEntity entity) {
        if (entity == null) return null;
        Transfer transfer = new Transfer();
        transfer.setTransferId(entity.getTransferId());
        transfer.setAmount(entity.getAmount());
        transfer.setCreationDate(entity.getCreationDate());
        transfer.setApprovalDate(entity.getApprovalDate());
        transfer.setTransferStatus(entity.getTransferStatus() != null ? TransferStatus.valueOf(entity.getTransferStatus()) : null);
        return transfer;
    }
}

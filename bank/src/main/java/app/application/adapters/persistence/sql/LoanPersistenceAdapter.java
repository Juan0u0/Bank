package app.application.adapters.persistence.sql;

import app.application.adapters.persistence.sql.entities.LoanEntity;
import app.application.adapters.persistence.sql.repositories.LoanRepository;
import app.domain.models.Loan;
import app.domain.enums.LoanType;
import app.domain.enums.approvalFlows.LoanStatus;
import app.domain.ports.LoanPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanPersistenceAdapter implements LoanPort {

    private final LoanRepository repository;

    public LoanPersistenceAdapter(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsById(Long loanId) {
        return repository.existsByLoanId(loanId);
    }

    @Override
    public boolean existsByClientDocument(String clientDocument) {
        return !repository.findByClientDocument(clientDocument).isEmpty();
    }

    @Override
    public void save(Loan loan) {
        repository.save(toEntity(loan));
    }

    @Override
    public void update(Loan loan) {
        LoanEntity existing = repository.findByLoanId(loan.getLoanId());
        if (existing != null) {
            existing.setAmountApproved(loan.getAmountApproved());
            existing.setLoanStatus(loan.getLoanStatus() != null ? loan.getLoanStatus().name() : null);
            existing.setApprovalDate(loan.getApprovalDate());
            existing.setDisbursementDate(loan.getDisbursementDate());
            existing.setAccountNumber(loan.getBankAccount() != null ? loan.getBankAccount().getAccountNumber() : null);
            repository.save(existing);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long loanId) {
        repository.deleteByLoanId(loanId);
    }

    @Override
    public Loan findById(Long loanId) {
        LoanEntity entity = repository.findByLoanId(loanId);
        return entity != null ? toModel(entity) : null;
    }

    @Override
    public List<Loan> findByClientDocument(String clientDocument) {
        return repository.findByClientDocument(clientDocument)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByStatus(LoanStatus status) {
        return repository.findByLoanStatus(status.name())
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private LoanEntity toEntity(Loan loan) {
        if (loan == null) return null;
        LoanEntity entity = new LoanEntity();
        entity.setLoanId(loan.getLoanId());
        entity.setLoanType(loan.getLoanType() != null ? loan.getLoanType().name() : null);
        entity.setClientDocument(loan.getClient() != null ? loan.getClient().getDocument() : null);
        entity.setAmountRequested(loan.getAmountRequested());
        entity.setAmountApproved(loan.getAmountApproved());
        entity.setInterestRate(loan.getInterestRate());
        entity.setTerm(loan.getTerm());
        entity.setLoanStatus(loan.getLoanStatus() != null ? loan.getLoanStatus().name() : null);
        entity.setApprovalDate(loan.getApprovalDate());
        entity.setDisbursementDate(loan.getDisbursementDate());
        entity.setAccountNumber(loan.getBankAccount() != null ? loan.getBankAccount().getAccountNumber() : null);
        return entity;
    }

    private Loan toModel(LoanEntity entity) {
        if (entity == null) return null;
        Loan loan = new Loan();
        loan.setLoanId(entity.getLoanId());
        loan.setLoanType(entity.getLoanType() != null ? LoanType.valueOf(entity.getLoanType()) : null);
        loan.setAmountRequested(entity.getAmountRequested());
        loan.setAmountApproved(entity.getAmountApproved());
        loan.setInterestRate(entity.getInterestRate());
        loan.setTerm(entity.getTerm());
        loan.setLoanStatus(entity.getLoanStatus() != null ? LoanStatus.valueOf(entity.getLoanStatus()) : null);
        loan.setApprovalDate(entity.getApprovalDate());
        loan.setDisbursementDate(entity.getDisbursementDate());
        return loan;
    }
}

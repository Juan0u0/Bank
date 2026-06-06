package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.enums.sistemRoles.SistemRole;
import app.domain.exceptions.BusinessException;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.services.*;
import java.util.List;
import java.util.Set;

@Service
public class CompanyClientUseCase {
    
    @Autowired
    private FindAccount findAccount;
    @Autowired
    private FindLoan findLoan;
    @Autowired
    private ApproveTransfer approveTransfer;
    @Autowired
    private RejectTransfer rejectTransfer;

    public CompanyClientUseCase(
            FindAccount findAccount,
            FindLoan findLoan,
            ApproveTransfer approveTransfer,
            RejectTransfer rejectTransfer) {
        this.findAccount = findAccount;
        this.findLoan = findLoan;
        this.approveTransfer = approveTransfer;
        this.rejectTransfer = rejectTransfer;
    }
    
    public List<BankAccount> getCompanyAccounts(String companyDocument) throws BusinessException {
        return findAccount.findByClientDocument(companyDocument);
    }
    
    public List<Loan> getCompanyLoans(String companyDocument) throws BusinessException {
        return findLoan.findByClientDocument(companyDocument);
    }
    
    public void delegatePermissions(String companyAdminDocument, String userDocument, Set<String> permissions) throws BusinessException {
        // TODO: Implementar lógica de delegación de permisos
        throw new BusinessException("Funcionalidad de delegación de permisos en desarrollo");
    }
    
    public void approveTransfer(Long transferId, String companyAdminDocument, SistemRole userRole) throws BusinessException {
        approveTransfer.approveTransfer(transferId, userRole);
    }
    
    public void rejectTransfer(Long transferId, String companyAdminDocument, SistemRole userRole) throws BusinessException {
        rejectTransfer.rejectTransfer(transferId, userRole);
    }
}

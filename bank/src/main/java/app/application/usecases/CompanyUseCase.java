package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.enums.SistemRole;
import app.domain.exceptions.BusinessException;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.ApproveTransfer;
import app.domain.services.CreateUser;
import app.domain.services.ExecuteTransfer;
import app.domain.services.RejectTransfer;
import app.domain.services.UpdateUser;

@Service
public class CompanyUseCase {
    @Autowired
    private CreateUser createUser;
    @Autowired
    private UpdateUser updateUser;
    @Autowired
    private ApproveTransfer approveTransfer;
    @Autowired
    private RejectTransfer rejectTransfer;
    @Autowired
    private ExecuteTransfer executeTransfer;

    public CompanyUseCase(CreateUser createUser, UpdateUser updateUser, ApproveTransfer approveTransfer, 
                          RejectTransfer rejectTransfer, ExecuteTransfer executeTransfer) {
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.approveTransfer = approveTransfer;
        this.rejectTransfer = rejectTransfer;
        this.executeTransfer = executeTransfer;
    }
    public void createUser(User user) throws BusinessException {
        createUser.createUser(user);
    }
    public void updateUser(User user) throws BusinessException {
        updateUser.updateUser(user);
    }
    public void approveTransfer (Long transferId, SistemRole userRole) throws BusinessException {
        approveTransfer.approveTransfer(transferId, userRole);
    }
    public void rejectTransfer (Long transferId, SistemRole userRole) throws BusinessException {
        rejectTransfer.rejectTransfer(transferId, userRole);
    }
     public void executeTransfer (Transfer transfer, String userDocument) throws BusinessException {
        executeTransfer.executeTransfer(transfer, userDocument);
     }
    }

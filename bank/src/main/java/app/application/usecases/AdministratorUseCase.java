package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.exceptions.BusinessException;
import app.domain.models.BankProduct;
import app.domain.services.CreateBankProduct;
import app.domain.services.DeleteBankProduct;
import app.domain.services.DeleteClient;
import app.domain.services.DeleteUser;
import app.domain.services.UpdateBankProduct;

@Service
public class AdministratorUseCase {
    @Autowired
    private CreateBankProduct createBankProduct;
    @Autowired
    private UpdateBankProduct updateBankProduct;
    @Autowired
    private DeleteBankProduct deleteBankProduct;
    @Autowired
    private DeleteUser deleteUser;
    @Autowired
    private DeleteClient deleteClient;

    public AdministratorUseCase(CreateBankProduct createBankProduct, UpdateBankProduct updateBankProduct, 
                                DeleteBankProduct deleteBankProduct, DeleteUser deleteUser, DeleteClient deleteClient) {
        this.createBankProduct = createBankProduct;
        this.updateBankProduct = updateBankProduct;
        this.deleteBankProduct = deleteBankProduct;
        this.deleteUser = deleteUser;
        this.deleteClient = deleteClient;
    }
    public void createBankProduct (BankProduct product) throws BusinessException {
        createBankProduct.createProduct(product);
    }
    public void updateBankProduct (BankProduct product) throws BusinessException {
        updateBankProduct.updateProduct(product);
    }
    public void deleteBankProduct (Long productId) throws BusinessException {
        deleteBankProduct.deleteProduct(productId);
    }
    public void deleteUser (String userDocument) throws BusinessException {
        deleteUser.deleteUser(userDocument);
    }
    public void deleteClient (String clientDocument) throws BusinessException {
        deleteClient.deleteClient(clientDocument);
    }
}

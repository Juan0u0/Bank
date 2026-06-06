package app.domain.services;

import app.domain.models.BankProduct;
import app.domain.ports.BankProductPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateBankProduct {
    
    private final BankProductPort productPort;
    
    @Autowired
    public UpdateBankProduct(BankProductPort productPort) {
        this.productPort = productPort;
    }
    
    public void updateProduct(BankProduct product) throws BusinessException {
        BankProduct existingProduct = productPort.findById(product.getProductId());
        if (existingProduct == null) {
            throw new BusinessException("El producto no existe");
        }
        
        productPort.update(product);
    }
}

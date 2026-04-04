package app.domain.services;

import app.domain.ports.BankProductPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteBankProduct {
    
    private final BankProductPort productPort;
    
    @Autowired
    public DeleteBankProduct(BankProductPort productPort) {
        this.productPort = productPort;
    }
    
    public void deleteProduct(Long productId) throws BusinessException {
        if (!productPort.existsById(productId)) {
            throw new BusinessException("El producto no existe");
        }
        productPort.deleteById(productId);
    }
}

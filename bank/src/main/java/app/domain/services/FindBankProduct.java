package app.domain.services;

import app.domain.models.BankProduct;
import app.domain.ports.BankProductPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindBankProduct {
    
    private final BankProductPort productPort;
    
    @Autowired
    public FindBankProduct(BankProductPort productPort) {
        this.productPort = productPort;
    }
    
    public BankProduct findById(Long productId) throws BusinessException {
        BankProduct product = productPort.findById(productId);
        if (product == null) {
            throw new BusinessException("El producto no existe");
        }
        return product;
    }
    
    public List<BankProduct> findByCategory(String category) {
        return productPort.findByCategory(category);
    }
    
    public List<BankProduct> findAll() {
        return productPort.findAll();
    }
}

package app.domain.services;

import app.domain.models.BankProduct;
import app.domain.ports.BankProductPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateBankProduct {
    
    private final BankProductPort productPort;
    
    @Autowired
    public CreateBankProduct(BankProductPort productPort) {
        this.productPort = productPort;
    }
    
    public void createProduct(BankProduct product) throws BusinessException {
        validateProductData(product);
        
        if (productPort.existsById(product.getProductId())) {
            throw new BusinessException("Ya existe un producto bancario con ese código");
        }
        
        productPort.save(product);
    }
    
    private void validateProductData(BankProduct product) throws BusinessException {
        if (product.getProductName() == null || product.getProductName().isBlank()) {
            throw new BusinessException("El nombre del producto es obligatorio");
        }
        
        if (product.getCategory() == null) {
            throw new BusinessException("La categoría del producto es obligatoria");
        }
    }
}

package app.application.adapters.api.controllers;

import app.application.adapters.api.request.BankProductRequest;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.AdministratorUseCase;
import app.domain.models.BankProduct;
import app.domain.exceptions.BusinessException;
import app.domain.enums.ProductCategory;
import app.domain.enums.ProductApproval;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    @Autowired
    private AdministratorUseCase administratorUseCase;

    public AdministratorController(AdministratorUseCase administratorUseCase) {
        this.administratorUseCase = administratorUseCase;
    }

    @PostMapping("/bank-products")
    public ResponseEntity<ApiResponse<String>> createBankProduct(
            @Valid @RequestBody BankProductRequest request) {
        try {
            BankProduct product = new BankProduct();
            product.setProductName(request.getProductName());
            product.setCategory(ProductCategory.valueOf(request.getCategory().toUpperCase()));
            product.setStatus(ProductApproval.valueOf(request.getStatus().toUpperCase()));
            
            administratorUseCase.createBankProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Producto bancario creado exitosamente", request.getProductName())
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PutMapping("/bank-products/{productId}")
    public ResponseEntity<ApiResponse<String>> updateBankProduct(
            @PathVariable Long productId,
            @Valid @RequestBody BankProductRequest request) {
        try {
            BankProduct product = new BankProduct();
            product.setProductId(productId);
            product.setProductName(request.getProductName());
            product.setCategory(ProductCategory.valueOf(request.getCategory().toUpperCase()));
            product.setStatus(ProductApproval.valueOf(request.getStatus().toUpperCase()));
            
            administratorUseCase.updateBankProduct(product);
            return ResponseEntity.ok(new ApiResponse<>(true, "Producto bancario actualizado exitosamente", request.getProductName()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/bank-products/{productId}")
    public ResponseEntity<ApiResponse<String>> deleteBankProduct(@PathVariable Long productId) {
        try {
            administratorUseCase.deleteBankProduct(productId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Producto bancario eliminado exitosamente", productId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/users/{userDocument}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String userDocument) {
        try {
            administratorUseCase.deleteUser(userDocument);
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuario eliminado exitosamente", userDocument));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/clients/{clientDocument}")
    public ResponseEntity<ApiResponse<String>> deleteClient(@PathVariable String clientDocument) {
        try {
            administratorUseCase.deleteClient(clientDocument);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cliente eliminado exitosamente", clientDocument));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }
}

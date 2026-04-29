package app.application.adapters.api.controllers;

import app.application.adapters.api.request.TransferRequest;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.CompanyEmployeeUseCase;
import app.domain.models.Transfer;
import app.domain.exceptions.BusinessException;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company-employee")
public class CompanyEmployeeController {

    @Autowired
    private CompanyEmployeeUseCase companyEmployeeUseCase;
    
    @Autowired
    private JwtUtil jwtUtil;

    public CompanyEmployeeController(CompanyEmployeeUseCase companyEmployeeUseCase, JwtUtil jwtUtil) {
        this.companyEmployeeUseCase = companyEmployeeUseCase;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/transfers")
    public ResponseEntity<ApiResponse<String>> createTransfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String userDocument = extractDocumentFromToken(token);
            Transfer transfer = new Transfer();
            transfer.setAmount(request.getAmount());
            
            companyEmployeeUseCase.createTransfer(transfer, request.getOriginAccountNumber(), 
                                                 request.getDestinationAccountNumber(), userDocument);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Transferencia creada exitosamente", "Pendiente")
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/accounts/{clientDocument}")
    public ResponseEntity<ApiResponse<String>> findAccount(@PathVariable String clientDocument) {
        try {
            companyEmployeeUseCase.findAccount(clientDocument);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta encontrada", clientDocument));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/transfers/{transferId}")
    public ResponseEntity<ApiResponse<String>> findTransfer(@PathVariable Long transferId) {
        try {
            companyEmployeeUseCase.findTransfer(transferId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia encontrada", transferId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

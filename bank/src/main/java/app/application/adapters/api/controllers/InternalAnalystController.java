package app.application.adapters.api.controllers;

import app.application.adapters.api.request.ApproveLoanRequest;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.InternalAnalystUseCase;
import app.domain.models.BankAccount;
import app.domain.exceptions.BusinessException;
import app.domain.enums.SistemRole;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal-analyst")
public class InternalAnalystController {

    @Autowired
    private InternalAnalystUseCase internalAnalystUseCase;
    
    @Autowired
    private JwtUtil jwtUtil;

    public InternalAnalystController(InternalAnalystUseCase internalAnalystUseCase, JwtUtil jwtUtil) {
        this.internalAnalystUseCase = internalAnalystUseCase;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/loans/{loanId}/approve")
    public ResponseEntity<ApiResponse<String>> approveLoan(
            @PathVariable Long loanId,
            @Valid @RequestBody ApproveLoanRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
            internalAnalystUseCase.approveLoan(loanId, request.getAmountApproved(), SistemRole.valueOf(userRole));
            return ResponseEntity.ok(new ApiResponse<>(true, "Préstamo aprobado exitosamente", loanId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/loans/{loanId}/reject")
    public ResponseEntity<ApiResponse<String>> rejectLoan(
            @PathVariable Long loanId,
            @RequestHeader("Authorization") String token) {
        try {
            String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
            internalAnalystUseCase.rejectLoan(loanId, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(new ApiResponse<>(true, "Préstamo rechazado exitosamente", loanId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/loans/{loanId}/disburse")
    public ResponseEntity<ApiResponse<String>> disburseLoan(
            @PathVariable Long loanId,
            @RequestHeader("Authorization") String token) {
        try {
            String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
            internalAnalystUseCase.disburseLoan(loanId, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(new ApiResponse<>(true, "Préstamo desembolsado exitosamente", loanId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/operation-logs/{logId}")
    public ResponseEntity<ApiResponse<String>> findOperationLog(@PathVariable Long logId) {
        try {
            internalAnalystUseCase.findOperationLog(logId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Bitácora encontrada", logId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/accounts/{accountNumber}/block")
    public ResponseEntity<ApiResponse<String>> blockAccount(@PathVariable String accountNumber) {
        try {
            BankAccount account = new BankAccount();
            account.setAccountNumber(accountNumber);
            internalAnalystUseCase.blockAccount(account);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta bloqueada exitosamente", accountNumber));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/accounts/{accountNumber}/unblock")
    public ResponseEntity<ApiResponse<String>> unblockAccount(@PathVariable String accountNumber) {
        try {
            BankAccount account = new BankAccount();
            account.setAccountNumber(accountNumber);
            internalAnalystUseCase.unblockAccount(account);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta desbloqueada exitosamente", accountNumber));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/accounts/{accountNumber}/close")
    public ResponseEntity<ApiResponse<String>> closeAccount(@PathVariable String accountNumber) {
        try {
            BankAccount account = new BankAccount();
            account.setAccountNumber(accountNumber);
            internalAnalystUseCase.closeAccount(account);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta cerrada exitosamente", accountNumber));
        } catch (Exception e) {
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

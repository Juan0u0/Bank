package app.application.adapters.api.controllers;

import app.application.adapters.api.request.CreateAccountRequest;
import app.application.adapters.api.request.DepositRequest;
import app.application.adapters.api.request.WithdrawRequest;
import app.application.adapters.api.response.ApiResponse;

import app.application.usecases.WindowEmployeeUseCase;
import app.domain.models.BankAccount;
import app.domain.exceptions.BusinessException;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/window-employee")
public class WindowEmployeeController {

    @Autowired
    private WindowEmployeeUseCase windowEmployeeUseCase;
    
    @Autowired
    private JwtUtil jwtUtil;

    public WindowEmployeeController(WindowEmployeeUseCase windowEmployeeUseCase, JwtUtil jwtUtil) {
        this.windowEmployeeUseCase = windowEmployeeUseCase;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<String>> createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String userDocument = extractDocumentFromToken(token);
            BankAccount account = new BankAccount();
            account.setAccountType(app.domain.enums.AccountType.valueOf(request.getAccountType().toUpperCase()));
            account.setBalance(request.getBalance());
            account.setCurrency(app.domain.enums.Currency.valueOf(request.getCurrency().toUpperCase()));
            
            windowEmployeeUseCase.createAccount(account, request.getClientDocument());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Cuenta creada exitosamente", account.getAccountNumber())
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/deposits")
    public ResponseEntity<ApiResponse<String>> depositMoney(
            @Valid @RequestBody DepositRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String userDocument = extractDocumentFromToken(token);
            windowEmployeeUseCase.depositMoney(request.getAccountNumber(), request.getAmount(), userDocument);
            return ResponseEntity.ok(new ApiResponse<>(true, "Depósito realizado exitosamente", request.getAccountNumber()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/withdrawals")
    public ResponseEntity<ApiResponse<String>> withdrawMoney(
            @Valid @RequestBody WithdrawRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String userDocument = extractDocumentFromToken(token);
            windowEmployeeUseCase.withdrawMoney(request.getAccountNumber(), request.getAmount(), userDocument);
            return ResponseEntity.ok(new ApiResponse<>(true, "Retiro realizado exitosamente", request.getAccountNumber()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<ApiResponse<String>> findAccount(@PathVariable String accountNumber) {
        try {
            windowEmployeeUseCase.findAccount(accountNumber);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta encontrada", accountNumber));
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

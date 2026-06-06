package app.application.adapters.api.controllers;

import app.application.adapters.api.request.RequestLoanRequest;
import app.application.adapters.api.request.TransferRequest;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.NaturalClientUseCase;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.models.BankAccount;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class NaturalClientController {

    private final NaturalClientUseCase clientUseCase;
    private final JwtUtil jwtUtil;

    // ── Ver productos propios (cuentas y préstamos) ───────────────────────────

    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<Object>> getMyProducts(
            @RequestHeader("Authorization") String token) {
        String clientDocument = extractDocumentFromToken(token);
        
        List<BankAccount> accounts = clientUseCase.getClientAccounts(clientDocument);
        List<Loan> loans = clientUseCase.getClientLoans(clientDocument);
        
        Map<String, Object> products = new java.util.HashMap<>();
        products.put("accounts", accounts);
        products.put("loans", loans);
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Productos consultados exitosamente", products)
        );
    }
    
    // ── Solicitar préstamo ───────────────────────────────────────────────────

    @PostMapping("/loans/request")
    public ResponseEntity<ApiResponse<String>> requestLoan(
            @Valid @RequestBody RequestLoanRequest request,
            @RequestHeader("Authorization") String token) {
        // Usar directamente el documento del cliente del request (puede ser NIT o documento)
        String clientDocument = request.getClientDocument().trim();
        
        Loan loan = new Loan();
        loan.setClientDocument(clientDocument);
        loan.setLoanType(request.getLoanType());
        loan.setAmountRequested(request.getAmountRequested());
        loan.setInterestRate(request.getInterestRate());
        loan.setTerm(request.getTermMonths());
        
        clientUseCase.RequestLoan(loan, clientDocument);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Solicitud de préstamo registrada exitosamente. Estado: En estudio", "En estudio")
        );
    }

    // ── Crear transferencia ──────────────────────────────────────────────────

    @PostMapping("/transfers")
    public ResponseEntity<ApiResponse<String>> createTransfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        String clientDocument = extractDocumentFromToken(token);
        Transfer transfer = new Transfer();
        transfer.setAmount(request.getAmount());
        
        clientUseCase.CreateTransfer(transfer, request.getOriginAccountNumber(), 
                                    request.getDestinationAccountNumber(), clientDocument);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Transferencia creada exitosamente", "Pendiente")
        );
    }

    // ── Consultar bitácora personal ──────────────────────────────────────────

    @GetMapping("/my-history")
    public ResponseEntity<ApiResponse<String>> getMyHistory(
            @RequestHeader("Authorization") String token) {
        String clientDocument = extractDocumentFromToken(token);
        
        String personalAuditLog = clientUseCase.getPersonalAuditLog(clientDocument);
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Bitácora personal consultada exitosamente", personalAuditLog)
        );
    }

    // ── Métodos auxiliares (Legacy - backwards compatibility) ────────────────

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<ApiResponse<String>> findAccount(@PathVariable String accountNumber) {
        clientUseCase.FindAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta encontrada", accountNumber));
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<ApiResponse<String>> findLoan(@PathVariable Long loanId) {
        clientUseCase.FindLoan(loanId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Préstamo encontrado", loanId.toString()));
    }

    @GetMapping("/transfers/{transferId}")
    public ResponseEntity<ApiResponse<String>> findTransfer(@PathVariable Long transferId) {
        clientUseCase.FindTransfer(transferId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia encontrada", transferId.toString()));
    }

    // ── Métodos auxiliares ───────────────────────────────────────────────────

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

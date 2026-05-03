package app.application.adapters.api.controllers;

import app.application.adapters.api.request.ApproveLoanRequest;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.InternalAnalystUseCase;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.models.OperationLog;
import app.domain.enums.sistemRoles.SistemRole;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal-analyst")
@RequiredArgsConstructor
public class InternalAnalystController {

    private final InternalAnalystUseCase internalAnalystUseCase;
    private final JwtUtil jwtUtil;

    // ── Consultar solicitudes de préstamos en estudio ────────────────────────

    @GetMapping("/loans/pending")
    public ResponseEntity<ApiResponse<List<Loan>>> getPendingLoans(
            @RequestHeader("Authorization") String token) {
        List<Loan> pendingLoans = internalAnalystUseCase.getPendingLoans();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Solicitudes en estudio consultadas exitosamente", pendingLoans)
        );
    }

    // ── Evaluar solicitud de préstamo (Único rol autorizado) ────────────────

    @PutMapping("/loans/{id}/evaluate")
    public ResponseEntity<ApiResponse<String>> evaluateLoan(
            @PathVariable Long id,
            @Valid @RequestBody ApproveLoanRequest request,
            @RequestHeader("Authorization") String token) {
        String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            internalAnalystUseCase.approveLoan(id, request.getAmountApproved(), SistemRole.valueOf(userRole));
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Préstamo aprobado exitosamente", id.toString())
            );
        }

        if ("REJECT".equalsIgnoreCase(request.getAction())) {
            internalAnalystUseCase.rejectLoan(id, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Préstamo rechazado exitosamente", id.toString())
            );
        }

        return ResponseEntity.badRequest().body(
            new ApiResponse<>(false, "Acción no válida. Use APPROVE o REJECT", null)
        );
    }

    // ── Aprobar o rechazar solicitud de préstamo - Legacy ────────────────────

    @PutMapping("/loans/{id}/approve-reject")
    public ResponseEntity<ApiResponse<String>> approveOrRejectLoan(
            @PathVariable Long id,
            @Valid @RequestBody ApproveLoanRequest request,
            @RequestHeader("Authorization") String token) {
        return evaluateLoan(id, request, token);
    }

    // ── Ejecutar desembolso de préstamo ──────────────────────────────────────

    @PostMapping("/loans/{id}/disburse")
    public ResponseEntity<ApiResponse<String>> disburseLoan(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
        internalAnalystUseCase.disburseLoan(id, SistemRole.valueOf(userRole));

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Préstamo desembolsado exitosamente", id.toString())
        );
    }

    // ── Acceso a la Bitácora completa (Consulta NoSQL) ───────────────────────

    @GetMapping("/audit/full-log")
    public ResponseEntity<ApiResponse<List<OperationLog>>> getFullAuditLog(
            @RequestHeader("Authorization") String token) {
        List<OperationLog> auditLog = internalAnalystUseCase.getCompleteAuditLog();

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Bitácora de operaciones completa consultada exitosamente", auditLog)
        );
    }

    @GetMapping("/audit/log")
    public ResponseEntity<ApiResponse<List<OperationLog>>> getAuditLog(
            @RequestHeader("Authorization") String token) {
        return getFullAuditLog(token);
    }

    @GetMapping("/operation-logs/{logId}")
    public ResponseEntity<ApiResponse<String>> findOperationLog(@PathVariable Long logId) {
        internalAnalystUseCase.findOperationLog(logId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Bitácora encontrada", logId.toString()));
    }

    @PostMapping("/accounts/{accountNumber}/block")
    public ResponseEntity<ApiResponse<String>> blockAccount(@PathVariable String accountNumber) {
        BankAccount account = new BankAccount();
        account.setAccountNumber(accountNumber);
        internalAnalystUseCase.blockAccount(account);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta bloqueada exitosamente", accountNumber));
    }

    @PostMapping("/accounts/{accountNumber}/unblock")
    public ResponseEntity<ApiResponse<String>> unblockAccount(@PathVariable String accountNumber) {
        BankAccount account = new BankAccount();
        account.setAccountNumber(accountNumber);
        internalAnalystUseCase.unblockAccount(account);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta desbloqueada exitosamente", accountNumber));
    }

    @PostMapping("/accounts/{accountNumber}/close")
    public ResponseEntity<ApiResponse<String>> closeAccount(@PathVariable String accountNumber) {
        BankAccount account = new BankAccount();
        account.setAccountNumber(accountNumber);
        internalAnalystUseCase.closeAccount(account);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta cerrada exitosamente", accountNumber));
    }

    // ── Métodos auxiliares ───────────────────────────────────────────────────

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

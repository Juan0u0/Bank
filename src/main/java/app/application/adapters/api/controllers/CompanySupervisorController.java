package app.application.adapters.api.controllers;

import app.application.adapters.api.request.*;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.CompanyUseCase;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.enums.sistemRoles.SistemRole;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-supervisor")
@RequiredArgsConstructor
public class CompanySupervisorController {

    private final CompanyUseCase companyUseCase;
    private final JwtUtil jwtUtil;

    // ── Consultar transferencias de alto monto en espera de aprobación ───────

    @GetMapping("/company/pending-transfers")
    public ResponseEntity<ApiResponse<List<Transfer>>> getPendingTransfers(
            @RequestHeader("Authorization") String token) {
        String supervisorDocument = extractDocumentFromToken(token);
        List<Transfer> pendingTransfers = companyUseCase.getPendingTransfersForCompany(supervisorDocument);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Transferencias en espera consultadas exitosamente", pendingTransfers)
        );
    }

    // ── Autorizar (aprobar/rechazar) transferencias - Endpoint según guía ─────

    @PutMapping("/transfers/{id}/decision")
    public ResponseEntity<ApiResponse<String>> makeTransferDecision(
            @PathVariable Long id,
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            companyUseCase.approveTransfer(id, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Transferencia aprobada exitosamente", id.toString())
            );
        }

        if ("REJECT".equalsIgnoreCase(request.getAction())) {
            companyUseCase.rejectTransfer(id, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Transferencia rechazada exitosamente", id.toString())
            );
        }

        return ResponseEntity.badRequest().body(
            new ApiResponse<>(false, "Acción no válida. Use APPROVE o REJECT", null)
        );
    }

    // ── Autorizar (aprobar/rechazar) transferencias - Legacy ───────────────────

    @PutMapping("/transfers/{id}/authorize")
    public ResponseEntity<ApiResponse<String>> authorizeTransfer(
            @PathVariable Long id,
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        return makeTransferDecision(id, request, token);
    }

    // ── Gestionar usuarios operativos de la empresa (PUT/DELETE) ──────────────

    @PostMapping("/company/manage-staff")
    public ResponseEntity<ApiResponse<String>> createStaffUser(
            @Valid @RequestBody UserRequest request) {
        User user = new User();
        user.setDocument(request.getDocument());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setCellPhone(request.getCellPhone());
        user.setAdress(request.getAdress());

        companyUseCase.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Usuario de personal creado exitosamente", request.getUsername())
        );
    }

    @PutMapping("/company/manage-staff")
    public ResponseEntity<ApiResponse<String>> updateStaffUser(
            @Valid @RequestBody UserRequest request) {
        User user = new User();
        user.setDocument(request.getDocument());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setCellPhone(request.getCellPhone());
        user.setAdress(request.getAdress());

        companyUseCase.updateUser(user);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Usuario de personal actualizado exitosamente", request.getUsername())
        );
    }

    @DeleteMapping("/company/manage-staff/{userDocument}")
    public ResponseEntity<ApiResponse<String>> deleteStaffUser(
            @PathVariable String userDocument) {
        companyUseCase.deleteUser(userDocument);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Usuario de personal eliminado exitosamente", userDocument)
        );
    }

    // ── Métodos auxiliares (Legacy - backwards compatibility) ────────────────

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<String>> createUser(
            @Valid @RequestBody UserRequest request) {
        User user = new User();
        user.setDocument(request.getDocument());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setCellPhone(request.getCellPhone());
        user.setAdress(request.getAdress());

        companyUseCase.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Usuario creado exitosamente", request.getUsername())
        );
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<String>> updateUser(
            @Valid @RequestBody UserRequest request) {
        User user = new User();
        user.setDocument(request.getDocument());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setCellPhone(request.getCellPhone());
        user.setAdress(request.getAdress());

        companyUseCase.updateUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario actualizado exitosamente", request.getUsername()));
    }

    @PostMapping("/transfers/{transferId}/approve")
    public ResponseEntity<ApiResponse<String>> approveTransfer(
            @PathVariable Long transferId,
            @RequestHeader("Authorization") String token) {
        String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
        companyUseCase.approveTransfer(transferId, SistemRole.valueOf(userRole));
        return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia aprobada exitosamente", transferId.toString()));
    }

    @PostMapping("/transfers/{transferId}/reject")
    public ResponseEntity<ApiResponse<String>> rejectTransfer(
            @PathVariable Long transferId,
            @RequestHeader("Authorization") String token) {
        String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
        companyUseCase.rejectTransfer(transferId, SistemRole.valueOf(userRole));
        return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia rechazada exitosamente", transferId.toString()));
    }

    @PostMapping("/transfers/execute")
    public ResponseEntity<ApiResponse<String>> executeTransfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        String userDocument = extractDocumentFromToken(token);
        Transfer transfer = new Transfer();
        transfer.setAmount(request.getAmount());

        companyUseCase.executeTransfer(transfer, userDocument);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia ejecutada exitosamente", "Ejecutada"));
    }

    // ── Métodos auxiliares ───────────────────────────────────────────────────

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

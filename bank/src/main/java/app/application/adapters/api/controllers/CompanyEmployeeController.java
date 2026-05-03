package app.application.adapters.api.controllers;

import app.application.adapters.api.request.*;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.CompanyEmployeeUseCase;
import app.domain.models.Transfer;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-employee")
@RequiredArgsConstructor
public class CompanyEmployeeController {

    private final CompanyEmployeeUseCase companyEmployeeUseCase;
    private final JwtUtil jwtUtil;

    // ── Crear transferencias y pagos masivos ─────────────────────────────────

    @PostMapping("/transfers/bulk")
    public ResponseEntity<ApiResponse<List<Transfer>>> createBulkTransfers(
            @Valid @RequestBody BulkTransferRequest request,
            @RequestHeader("Authorization") String token) {
        String userDocument = extractDocumentFromToken(token);

        List<Transfer> result = companyEmployeeUseCase.processBulkTransfers(
            userDocument,
            request.getTransfers()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Transferencias masivas procesadas exitosamente", result)
        );
    }

    // ── Métodos auxiliares ───────────────────────────────────────────────────

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

package app.application.adapters.api.controllers;

import app.application.adapters.api.request.*;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.CompanyClientUseCase;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.enums.sistemRoles.SistemRole;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/company-client")
@RequiredArgsConstructor
public class CompanyClientController {

    private final CompanyClientUseCase companyClientUseCase;
    private final JwtUtil jwtUtil;

    // ── Visualizar productos de la empresa ────────────────────────────────────

    @GetMapping("/company/products")
    public ResponseEntity<ApiResponse<Object>> getCompanyProducts(
            @RequestHeader("Authorization") String token) {
        String companyDocument = extractDocumentFromToken(token);

        List<BankAccount> accounts = companyClientUseCase.getCompanyAccounts(companyDocument);
        List<Loan> loans = companyClientUseCase.getCompanyLoans(companyDocument);

        Map<String, Object> products = new HashMap<>();
        products.put("accounts", accounts);
        products.put("loans", loans);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Productos de empresa consultados exitosamente", products)
        );
    }

    // ── Delegar permisos a usuarios operativos ───────────────────────────────

    @PutMapping("/company/permissions")
    public ResponseEntity<ApiResponse<String>> delegatePermissions(
            @Valid @RequestBody PermissionsRequest request,
            @RequestHeader("Authorization") String token) {
        String companyAdminDocument = extractDocumentFromToken(token);

        companyClientUseCase.delegatePermissions(
            companyAdminDocument,
            request.getUserDocument(),
            request.getPermissions()
        );

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Permisos delegados exitosamente", request.getUserDocument())
        );
    }

    // ── Aprobar transferencias de alto valor ─────────────────────────────────

    @PutMapping("/transfers/{id}/approve")
    public ResponseEntity<ApiResponse<String>> approveHighValueTransfer(
            @PathVariable Long id,
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        String companyAdminDocument = extractDocumentFromToken(token);
        String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            companyClientUseCase.approveTransfer(id, companyAdminDocument, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Transferencia aprobada exitosamente", id.toString())
            );
        }

        if ("REJECT".equalsIgnoreCase(request.getAction())) {
            companyClientUseCase.rejectTransfer(id, companyAdminDocument, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Transferencia rechazada exitosamente", id.toString())
            );
        }

        return ResponseEntity.badRequest().body(
            new ApiResponse<>(false, "Acción no válida. Use APPROVE o REJECT", null)
        );
    }

    // ── Métodos auxiliares ───────────────────────────────────────────────────

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

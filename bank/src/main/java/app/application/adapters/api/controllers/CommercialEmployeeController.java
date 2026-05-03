package app.application.adapters.api.controllers;

import app.application.adapters.api.request.*;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.CommercialEmployeeUseCase;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commercial-employee")
@RequiredArgsConstructor
public class CommercialEmployeeController {

    private final CommercialEmployeeUseCase commercialEmployeeUseCase;
    private final JwtUtil jwtUtil;

    // ── Consultar información de clientes bajo gestión ───────────────────────

    @GetMapping("/clients/managed")
    public ResponseEntity<ApiResponse<List<?>>> getManagedClients(
            @RequestHeader("Authorization") String token) {
        String employeeDocument = extractDocumentFromToken(token);

        List<?> managedClients = commercialEmployeeUseCase.getManagedClients(employeeDocument);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Clientes bajo gestión consultados exitosamente", managedClients)
        );
    }

    // ── Crear solicitudes de productos en nombre del cliente ──────────────────

    @PostMapping("/products/apply")
    public ResponseEntity<ApiResponse<String>> applyForProduct(
            @Valid @RequestBody ProductApplicationRequest request,
            @RequestHeader("Authorization") String token) {
        String employeeDocument = extractDocumentFromToken(token);

        String result = commercialEmployeeUseCase.applyForProduct(
            employeeDocument,
            request.getClientDocument(),
            request.getProductType(),
            request.getProductDetails()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Solicitud de producto creada exitosamente", result)
        );
    }

    // ── Seguimiento de solicitudes de préstamos ───────────────────────────────

    @GetMapping("/loans/track")
    public ResponseEntity<ApiResponse<List<?>>> trackLoans(
            @RequestHeader("Authorization") String token) {
        String employeeDocument = extractDocumentFromToken(token);

        List<?> loanTracking = commercialEmployeeUseCase.trackManagedLoans(employeeDocument);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Seguimiento de préstamos obtenido exitosamente", loanTracking)
        );
    }

    // ── Métodos auxiliares ───────────────────────────────────────────────────

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

package app.application.adapters.api.controllers;

import app.application.adapters.api.request.*;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.CompanyUseCase;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.exceptions.BusinessException;
import app.domain.enums.SistemRole;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company-supervisor")
public class CompanySupervisorController {

    @Autowired
    private CompanyUseCase companyUseCase;
    
    @Autowired
    private JwtUtil jwtUtil;

    public CompanySupervisorController(CompanyUseCase companyUseCase, JwtUtil jwtUtil) {
        this.companyUseCase = companyUseCase;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<String>> createUser(
            @Valid @RequestBody UserRequest request) {
        try {
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
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<String>> updateUser(
            @Valid @RequestBody UserRequest request) {
        try {
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
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/transfers/{transferId}/approve")
    public ResponseEntity<ApiResponse<String>> approveTransfer(
            @PathVariable Long transferId,
            @RequestHeader("Authorization") String token) {
        try {
            String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
            companyUseCase.approveTransfer(transferId, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia aprobada exitosamente", transferId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/transfers/{transferId}/reject")
    public ResponseEntity<ApiResponse<String>> rejectTransfer(
            @PathVariable Long transferId,
            @RequestHeader("Authorization") String token) {
        try {
            String userRole = jwtUtil.extractRole(token.replace("Bearer ", ""));
            companyUseCase.rejectTransfer(transferId, SistemRole.valueOf(userRole));
            return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia rechazada exitosamente", transferId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/transfers/execute")
    public ResponseEntity<ApiResponse<String>> executeTransfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String userDocument = extractDocumentFromToken(token);
            Transfer transfer = new Transfer();
            transfer.setAmount(request.getAmount());
            
            companyUseCase.executeTransfer(transfer, userDocument);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia ejecutada exitosamente", "Ejecutada"));
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

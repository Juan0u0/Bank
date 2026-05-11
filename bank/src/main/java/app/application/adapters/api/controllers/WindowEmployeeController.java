package app.application.adapters.api.controllers;

import app.application.adapters.api.request.*;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.WindowEmployeeUseCase;
import app.application.adapters.api.request.NaturalPersonRequest;
import app.application.adapters.api.request.CompanyRequest;
import app.domain.models.NaturalPerson;
import app.domain.models.Company;
import app.domain.models.BankAccount;
import app.domain.models.User;
import app.domain.enums.sistemRoles.SistemRole;
import app.domain.enums.status.UserStatus;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/window-employee")
@RequiredArgsConstructor
public class WindowEmployeeController {

    private final WindowEmployeeUseCase windowEmployeeUseCase;
    private final JwtUtil jwtUtil;

    // ── Registro de nuevos usuarios/clientes ──────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(
            @Valid @RequestBody UserRequest request) {
        User user = new User();
        user.setDocument(request.getDocument());
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setCellPhone(request.getCellPhone());
        user.setAdress(request.getAdress());
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(SistemRole.valueOf(request.getRole().toUpperCase()));
        
        windowEmployeeUseCase.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Usuario registrado exitosamente", user.getDocument())
        );
    }

    // ── Apertura de cuentas ──────────────────────────────────────────────────

    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<String>> createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @RequestHeader("Authorization") String token) {
        BankAccount account = new BankAccount();
        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());

        windowEmployeeUseCase.createAccount(account, request.getClientDocument());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Cuenta creada exitosamente", account.getAccountNumber())
        );
    }

        @PostMapping("/clients/natural")
        public ResponseEntity<ApiResponse<String>> registerNatural(
            @Valid @RequestBody NaturalPersonRequest request) {
        NaturalPerson person = new NaturalPerson();
        person.setDocument(request.getDocument());
        person.setName(request.getName());
        person.setEmail(request.getEmail());
        person.setCellPhone(request.getCellPhone());
        person.setAdress(request.getAdress());
        person.setBirthDate(request.getBirthDate());

        windowEmployeeUseCase.registerNaturalPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Cliente natural registrado exitosamente", person.getDocument())
        );
        }

        @PostMapping("/clients/company")
        public ResponseEntity<ApiResponse<String>> registerCompany(
            @Valid @RequestBody CompanyRequest request) {
        Company company = new Company();
        company.setDocument(request.getDocument());
        company.setName(request.getName());
        company.setCompanyName(request.getCompanyName());
        company.setTaxId(request.getTaxId());
        company.setLegalRepresentative(request.getLegalRepresentative());
        company.setEmail(request.getEmail());
        company.setCellPhone(request.getCellPhone());
        company.setAdress(request.getAdress());

        windowEmployeeUseCase.registerCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Empresa registrada exitosamente", company.getDocument())
        );
        }

    // ── Consultar saldo ──────────────────────────────────────────────────────

    @GetMapping("/accounts/{id}/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getAccountBalance(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        BankAccount account = windowEmployeeUseCase.getAccount(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Saldo consultado exitosamente", account.getBalance()));
    }

    // ── Depósitos y retiros ──────────────────────────────────────────────────

    @PostMapping("/transactions/deposit")
    public ResponseEntity<ApiResponse<String>> deposit(
            @Valid @RequestBody DepositRequest request,
            @RequestHeader("Authorization") String token) {
        String userDocument = extractDocumentFromToken(token);
        windowEmployeeUseCase.depositMoney(request.getAccountNumber(), request.getAmount(), userDocument);
        return ResponseEntity.ok(new ApiResponse<>(true, "Depósito realizado exitosamente", request.getAccountNumber()));
    }

    @PostMapping("/transactions/withdraw")
    public ResponseEntity<ApiResponse<String>> withdraw(
            @Valid @RequestBody WithdrawRequest request,
            @RequestHeader("Authorization") String token) {
        String userDocument = extractDocumentFromToken(token);
        windowEmployeeUseCase.withdrawMoney(request.getAccountNumber(), request.getAmount(), userDocument);
        return ResponseEntity.ok(new ApiResponse<>(true, "Retiro realizado exitosamente", request.getAccountNumber()));
    }

    // ── Métodos auxiliares ───────────────────────────────────────────────────

    private String extractDocumentFromToken(String token) {
        String bearerToken = token.replace("Bearer ", "");
        return jwtUtil.extractDocument(bearerToken);
    }
}

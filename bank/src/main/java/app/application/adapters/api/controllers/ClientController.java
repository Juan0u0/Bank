package app.application.adapters.api.controllers;

import app.application.adapters.api.request.RequestLoanRequest;
import app.application.adapters.api.request.TransferRequest;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.ClientUseCase;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.exceptions.BusinessException;
import app.domain.enums.LoanType;
import app.infraestructure.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientUseCase clientUseCase;
    
    @Autowired
    private JwtUtil jwtUtil;

    public ClientController(ClientUseCase clientUseCase, JwtUtil jwtUtil) {
        this.clientUseCase = clientUseCase;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<ApiResponse<String>> findAccount(@PathVariable String accountNumber) {
        try {
            clientUseCase.FindAccount(accountNumber);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta encontrada", accountNumber));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<ApiResponse<String>> findLoan(@PathVariable Long loanId) {
        try {
            clientUseCase.FindLoan(loanId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Préstamo encontrado", loanId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/transfers/{transferId}")
    public ResponseEntity<ApiResponse<String>> findTransfer(@PathVariable Long transferId) {
        try {
            clientUseCase.FindTransfer(transferId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transferencia encontrada", transferId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/loans/request")
    public ResponseEntity<ApiResponse<String>> requestLoan(
            @Valid @RequestBody RequestLoanRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String clientDocument = extractDocumentFromToken(token);
            Loan loan = new Loan();
            loan.setAmountRequested(request.getAmountRequested());
            loan.setInterestRate(request.getInterestRate());
            loan.setTerm(request.getTerm());
            loan.setLoanType(LoanType.valueOf(request.getLoanType().toUpperCase()));
            
            clientUseCase.RequestLoan(loan, clientDocument);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Solicitud de préstamo registrada exitosamente", "En estudio")
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/transfers")
    public ResponseEntity<ApiResponse<String>> createTransfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String clientDocument = extractDocumentFromToken(token);
            Transfer transfer = new Transfer();
            transfer.setAmount(request.getAmount());
            
            clientUseCase.CreateTransfer(transfer, request.getOriginAccountNumber(), 
                                        request.getDestinationAccountNumber(), clientDocument);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Transferencia creada exitosamente", "Pendiente")
            );
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

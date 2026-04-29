package app.application.adapters.api.controllers;

import app.application.adapters.api.request.*;
import app.application.adapters.api.response.ApiResponse;
import app.application.usecases.CommercialEmployeeUseCase;
import app.domain.models.*;
import app.domain.exceptions.BusinessException;
import app.domain.enums.LoanType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commercial-employee")
public class CommercialEmployeeController {

    @Autowired
    private CommercialEmployeeUseCase commercialEmployeeUseCase;

    public CommercialEmployeeController(CommercialEmployeeUseCase commercialEmployeeUseCase) {
        this.commercialEmployeeUseCase = commercialEmployeeUseCase;
    }

    @PostMapping("/natural-persons")
    public ResponseEntity<ApiResponse<String>> createNaturalPerson(
            @Valid @RequestBody NaturalPersonRequest request) {
        try {
            NaturalPerson naturalPerson = new NaturalPerson();
            naturalPerson.setDocument(request.getDocument());
            naturalPerson.setName(request.getName());
            naturalPerson.setEmail(request.getEmail());
            naturalPerson.setCellPhone(request.getCellPhone());
            naturalPerson.setAdress(request.getAdress());
            naturalPerson.setBirthDate(request.getBirthDate());
            
            commercialEmployeeUseCase.createNaturalPerson(naturalPerson);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Persona natural registrada exitosamente", request.getDocument())
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/companies")
    public ResponseEntity<ApiResponse<String>> createCompany(
            @Valid @RequestBody CompanyRequest request) {
        try {
            Company company = new Company();
            company.setDocument(request.getDocument());
            company.setName(request.getName());
            company.setCompanyName(request.getCompanyName());
            company.setTaxId(request.getTaxId());
            company.setLegalRepresentative(request.getLegalRepresentative());
            company.setEmail(request.getEmail());
            company.setCellPhone(request.getCellPhone());
            company.setAdress(request.getAdress());
            
            commercialEmployeeUseCase.createCompany(company);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Empresa registrada exitosamente", request.getDocument())
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
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
            
            commercialEmployeeUseCase.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Usuario creado exitosamente", request.getUsername())
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/loans/request")
    public ResponseEntity<ApiResponse<String>> requestLoan(
            @Valid @RequestBody RequestLoanRequest request,
            @RequestParam String clientDocument) {
        try {
            Loan loan = new Loan();
            loan.setAmountRequested(request.getAmountRequested());
            loan.setInterestRate(request.getInterestRate());
            loan.setTerm(request.getTerm());
            loan.setLoanType(LoanType.valueOf(request.getLoanType().toUpperCase()));
            
            commercialEmployeeUseCase.requestLoan(loan, clientDocument);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Solicitud de préstamo registrada exitosamente", "En estudio")
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<ApiResponse<String>> findLoan(@PathVariable Long loanId) {
        try {
            commercialEmployeeUseCase.findLoan(loanId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Préstamo encontrado", loanId.toString()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }
}

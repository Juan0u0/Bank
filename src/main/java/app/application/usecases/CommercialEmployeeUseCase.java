package app.application.usecases;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.exceptions.BusinessException;
import app.domain.models.Client;
import app.domain.models.Company;
import app.domain.models.Loan;
import app.domain.models.NaturalPerson;
import app.domain.models.User;
import app.domain.services.FindClient;
import app.domain.services.CreateCompany;
import app.domain.services.CreateNaturalPerson;
import app.domain.services.CreateUser;
import app.domain.services.FindLoan;
import app.domain.services.RequestLoan;
import app.domain.services.UpdateClient;

@Service
public class CommercialEmployeeUseCase {

    @Autowired
    private CreateCompany createCompany; 
    @Autowired
    private CreateNaturalPerson createNaturalPerson; 
    @Autowired
    private CreateUser createUser; 
    @Autowired
    private RequestLoan requestLoan; 
    @Autowired
    private UpdateClient updateClient; 
    @Autowired
    private FindLoan findLoan; 
    @Autowired
    private FindClient findClient;


    public CommercialEmployeeUseCase(   CreateCompany createCompany, CreateNaturalPerson createNaturalPerson, CreateUser createUser,
                                        RequestLoan requestLoan, UpdateClient updateClient, FindLoan findLoan, FindClient findClient) {
        this.createCompany = createCompany;
        this.createNaturalPerson = createNaturalPerson;
        this.createUser = createUser;
        this.requestLoan = requestLoan;
        this.updateClient = updateClient;
        this.findLoan = findLoan;
        this.findClient = findClient;
    }
    public void createCompany (Company company) throws BusinessException {
        createCompany.createCompany(company);
    }
    public void createNaturalPerson (NaturalPerson naturalPerson) throws BusinessException {
        createNaturalPerson.createNaturalPerson(naturalPerson);
    }
    public void createUser (User user) throws BusinessException {
        createUser.createUser(user);
    }
    public void requestLoan (Loan loan, String clientDocument) throws BusinessException {
        requestLoan.requestLoan(loan, clientDocument);
    }
    public void updateClient (Client client) throws BusinessException {
        updateClient.updateClient(client);
    }
    public void findLoan (Long loanId) throws BusinessException {
        findLoan.findById(loanId);
    }

    public List<Client> getManagedClients(String employeeDocument) throws BusinessException {
        return findClient.findAll();
    }

    public String applyForProduct(String employeeDocument, String clientDocument, String productType,
                                  Map<String, Object> productDetails) throws BusinessException {
        if (!"LOAN".equalsIgnoreCase(productType)) {
            throw new BusinessException("Tipo de producto no soportado");
        }

        Loan loan = new Loan();
        Object amountRequested = productDetails.get("amountRequested");
        Object interestRate = productDetails.get("interestRate");
        Object term = productDetails.get("term");

        if (amountRequested instanceof Number) {
            loan.setAmountRequested(new java.math.BigDecimal(amountRequested.toString()));
        }
        if (interestRate instanceof Number) {
            loan.setInterestRate(new java.math.BigDecimal(interestRate.toString()));
        }
        if (term instanceof Number) {
            loan.setTerm(((Number) term).intValue());
        }

        requestLoan.requestLoan(loan, clientDocument);
        return "Solicitud de producto creada";
    }

    public List<Loan> trackManagedLoans(String employeeDocument) throws BusinessException {
        return findLoan.findByClientDocument(employeeDocument);
    }
}
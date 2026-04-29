package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.exceptions.BusinessException;
import app.domain.models.Client;
import app.domain.models.Company;
import app.domain.models.Loan;
import app.domain.models.NaturalPerson;
import app.domain.models.User;
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


    public CommercialEmployeeUseCase(   CreateCompany createCompany, CreateNaturalPerson createNaturalPerson, CreateUser createUser,
                                        RequestLoan requestLoan, UpdateClient updateClient, FindLoan findLoan) {
        this.createCompany = createCompany;
        this.createNaturalPerson = createNaturalPerson;
        this.createUser = createUser;
        this.requestLoan = requestLoan;
        this.updateClient = updateClient;
        this.findLoan = findLoan;
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
}
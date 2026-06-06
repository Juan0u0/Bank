package app.domain.services;

import app.domain.models.Company;
import app.domain.ports.ClientPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class CreateCompany {
    
    private final ClientPort clientPort;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    @Autowired
    public CreateCompany(ClientPort clientPort) {
        this.clientPort = clientPort;
    }
    
    public void createCompany(Company company) throws BusinessException {
        validateCompanyData(company);
        
        if (clientPort.existsByDocument(company.getDocument())) {
            throw new BusinessException("Ya existe una empresa con ese NIT");
        }
        
        clientPort.save(company);
    }
    
    private void validateCompanyData(Company company) throws BusinessException {
        if (company.getName() == null || company.getName().isBlank()) {
            throw new BusinessException("La razón social es obligatoria");
        }
        
        if (company.getDocument() == null || company.getDocument().isBlank()) {
            throw new BusinessException("El NIT es obligatorio");
        }
        
        if (company.getEmail() == null || company.getEmail().isBlank() || !pattern.matcher(company.getEmail()).matches()) {
            throw new BusinessException("El correo electrónico es obligatorio y debe ser válido");
        }
        
        if (company.getCellPhone() == null || company.getCellPhone().isBlank()) {
            throw new BusinessException("El número de teléfono es obligatorio");
        }
        
        if (company.getCellPhone().length() < 7 || company.getCellPhone().length() > 15) {
            throw new BusinessException("El número de teléfono debe tener entre 7 y 15 dígitos");
        }
        
        if (company.getAdress() == null || company.getAdress().isBlank()) {
            throw new BusinessException("La dirección es obligatoria");
        }
    }
}

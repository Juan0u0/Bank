package app.domain.services;

import app.domain.models.NaturalPerson;
import app.domain.ports.ClientPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Service
public class CreateNaturalPerson {
    
    private final ClientPort clientPort;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    @Autowired
    public CreateNaturalPerson(ClientPort clientPort) {
        this.clientPort = clientPort;
    }
    
    public void createNaturalPerson(NaturalPerson naturalPerson) throws BusinessException {
        validateNaturalPersonData(naturalPerson);
        
        if (clientPort.existsByDocument(naturalPerson.getDocument())) {
            throw new BusinessException("Ya existe una persona natural con ese número de identificación");
        }
        
        clientPort.save(naturalPerson);
    }
    
    private void validateNaturalPersonData(NaturalPerson naturalPerson) throws BusinessException {
        if (naturalPerson.getName() == null || naturalPerson.getName().isBlank()) {
            throw new BusinessException("El nombre completo es obligatorio");
        }
        
        if (naturalPerson.getDocument() == null || naturalPerson.getDocument().isBlank()) {
            throw new BusinessException("El número de identificación es obligatorio");
        }
        
        if (naturalPerson.getEmail() == null || naturalPerson.getEmail().isBlank() || !pattern.matcher(naturalPerson.getEmail()).matches()) {
            throw new BusinessException("El correo electrónico es obligatorio y debe ser válido");
        }
        
        if (naturalPerson.getCellPhone() == null || naturalPerson.getCellPhone().isBlank()) {
            throw new BusinessException("El número de teléfono es obligatorio");
        }
        
        if (naturalPerson.getCellPhone().length() < 7 || naturalPerson.getCellPhone().length() > 15) {
            throw new BusinessException("El número de teléfono debe tener entre 7 y 15 dígitos");
        }
        
        if (naturalPerson.getAdress() == null || naturalPerson.getAdress().isBlank()) {
            throw new BusinessException("La dirección es obligatoria");
        }
        
        if (naturalPerson.getBirthDate() == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria");
        }
        
        int age = Period.between(naturalPerson.getBirthDate(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new BusinessException("El cliente debe ser mayor de edad (mínimo 18 años)");
        }
    }
}

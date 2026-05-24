package app.domain.services;

import app.domain.models.User;
import app.domain.models.NaturalPerson;
import app.domain.models.Company;
import app.domain.ports.UserPort;
import app.domain.ports.ClientPort;
import app.domain.enums.status.UserStatus;
import app.domain.enums.sistemRoles.SistemRole;
import app.domain.exceptions.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class CreateUser {
    
    private final UserPort userPort;
    private final ClientPort clientPort;
    private final PasswordEncoder passwordEncoder;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    public CreateUser(UserPort userPort, ClientPort clientPort, PasswordEncoder passwordEncoder) {
        this.userPort = userPort;
        this.clientPort = clientPort;
        this.passwordEncoder = passwordEncoder;
    }
    
    public void createUser(User user) throws BusinessException {
        validateUserData(user);
        
        if (userPort.existsByDocument(user.getDocument())) {
            throw new BusinessException("Ya existe un usuario con ese número de identificación");
        }
        
        if (userPort.existsByUsername(user.getUsername())) {
            throw new BusinessException("Ya existe un usuario con ese nombre de usuario");
        }

        // Si el usuario es NATURAL_CLIENT o CLIENT_COMPANY, crear el cliente automáticamente
        if (user.getRole() == SistemRole.NATURAL_CLIENT) {
            if (!clientPort.existsByDocument(user.getDocument())) {
                NaturalPerson naturalClient = new NaturalPerson();
                naturalClient.setName(user.getName());
                naturalClient.setDocument(user.getDocument());
                naturalClient.setEmail(user.getEmail());
                naturalClient.setCellPhone(user.getCellPhone());
                naturalClient.setAdress(user.getAdress());
                naturalClient.setBirthDate(user.getBirthDate());
                naturalClient.setRole(SistemRole.NATURAL_CLIENT);
                clientPort.save(naturalClient);
            }
        } else if (user.getRole() == SistemRole.CLIENT_COMPANY) {
            if (!clientPort.existsByDocument(user.getDocument())) {
                Company company = new Company();
                company.setName(user.getName());
                company.setDocument(user.getDocument());
                company.setEmail(user.getEmail());
                company.setCellPhone(user.getCellPhone());
                company.setAdress(user.getAdress());
                company.setRole(SistemRole.CLIENT_COMPANY);
                clientPort.save(company);
            }
        }
        
        // Encriptar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        userPort.save(user);
    }
    
    private void validateUserData(User user) throws BusinessException {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new BusinessException("El nombre completo es obligatorio");
        }
        
        if (user.getDocument() == null || user.getDocument().isBlank()) {
            throw new BusinessException("El número de identificación es obligatorio");
        }
        
        if (user.getEmail() == null || user.getEmail().isBlank() || !pattern.matcher(user.getEmail()).matches()) {
            throw new BusinessException("El correo electrónico es obligatorio y debe ser válido");
        }
        
        if (user.getCellPhone() == null || user.getCellPhone().isBlank()) {
            throw new BusinessException("El número de teléfono es obligatorio");
        }
        
        if (user.getCellPhone().length() < 7 || user.getCellPhone().length() > 15) {
            throw new BusinessException("El número de teléfono debe tener entre 7 y 15 dígitos");
        }
        
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BusinessException("El nombre de usuario es obligatorio");
        }
        
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BusinessException("La contraseña es obligatoria");
        }
        
        if (user.getRole() == null) {
            throw new BusinessException("El rol del usuario es obligatorio");
        }
    }
}

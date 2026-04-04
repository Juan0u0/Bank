package app.domain.services;

import app.domain.models.User;
import app.domain.enums.UserStatus;
import app.domain.ports.UserPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class CreateUser {
    
    private final UserPort userPort;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    @Autowired
    public CreateUser(UserPort userPort) {
        this.userPort = userPort;
    }
    
    public void createUser(User user) throws BusinessException {
        validateUserData(user);
        
        if (userPort.existsByDocument(user.getDocument())) {
            throw new BusinessException("Ya existe un usuario con ese número de identificación");
        }
        
        if (userPort.existsByUsername(user.getUsername())) {
            throw new BusinessException("Ya existe un usuario con ese nombre de usuario");
        }
        
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

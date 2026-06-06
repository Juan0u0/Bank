package app.domain.services;

import app.domain.models.User;
import app.domain.ports.UserPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class UpdateUser {
    
    private final UserPort userPort;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    @Autowired
    public UpdateUser(UserPort userPort) {
        this.userPort = userPort;
    }
    
    public void updateUser(User user) throws BusinessException {
        validateUserData(user);
        
        User existingUser = userPort.findByDocument(user.getDocument());
        if (existingUser == null) {
            throw new BusinessException("El usuario no existe");
        }
        
        // Validar que el nuevo username no esté en uso por otro usuario
        if (!existingUser.getUsername().equals(user.getUsername())) {
            if (userPort.existsByUsernameAndDocumentNot(user.getUsername(), user.getDocument())) {
                throw new BusinessException("Ya existe otro usuario con ese nombre de usuario");
            }
        }
        
        userPort.update(user);
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
    }
}

package app.domain.services;

import app.domain.models.User;
import app.domain.enums.UserStatus;
import app.domain.ports.UserPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUser {
    
    private final UserPort userPort;
    
    @Autowired
    public AuthenticateUser(UserPort userPort) {
        this.userPort = userPort;
    }
    
    public User authenticate(String username, String password) throws BusinessException {
        User user = userPort.findByUsername(username);
        
        if (user == null) {
            throw new BusinessException("Usuario o contraseña incorrectos");
        }
        
        if (!user.getPassword().equals(password)) {
            throw new BusinessException("Usuario o contraseña incorrectos");
        }
        
        if (user.getStatus() == UserStatus.INACTIVE || user.getStatus() == UserStatus.BLOCKED) {
            throw new BusinessException("El usuario está inactivo o bloqueado");
        }
        
        return user;
    }
    
    public void blockUser(String userDocument) throws BusinessException {
        User user = userPort.findByDocument(userDocument);
        if (user == null) {
            throw new BusinessException("El usuario no existe");
        }
        user.setStatus(UserStatus.BLOCKED);
        userPort.update(user);
    }
    
    public void activateUser(String userDocument) throws BusinessException {
        User user = userPort.findByDocument(userDocument);
        if (user == null) {
            throw new BusinessException("El usuario no existe");
        }
        user.setStatus(UserStatus.ACTIVE);
        userPort.update(user);
    }
    
    public void deactivateUser(String userDocument) throws BusinessException {
        User user = userPort.findByDocument(userDocument);
        if (user == null) {
            throw new BusinessException("El usuario no existe");
        }
        user.setStatus(UserStatus.INACTIVE);
        userPort.update(user);
    }
}

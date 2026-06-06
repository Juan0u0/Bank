package app.domain.services;

import app.domain.ports.UserPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUser {
    
    private final UserPort userPort;
    
    @Autowired
    public DeleteUser(UserPort userPort) {
        this.userPort = userPort;
    }
    
    public void deleteUser(String userDocument) throws BusinessException {
        if (!userPort.existsByDocument(userDocument)) {
            throw new BusinessException("El usuario no existe");
        }
        userPort.deleteByDocument(userDocument);
    }
}

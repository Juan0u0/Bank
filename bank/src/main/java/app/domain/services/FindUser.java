package app.domain.services;

import app.domain.models.User;
import app.domain.ports.UserPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindUser {
    
    private final UserPort userPort;
    
    @Autowired
    public FindUser(UserPort userPort) {
        this.userPort = userPort;
    }
    
    public User findByDocument(String document) throws BusinessException {
        User user = userPort.findByDocument(document);
        if (user == null) {
            throw new BusinessException("El usuario no existe");
        }
        return user;
    }
    
    public User findByUsername(String username) throws BusinessException {
        User user = userPort.findByUsername(username);
        if (user == null) {
            throw new BusinessException("El usuario no existe");
        }
        return user;
    }
    
    public List<User> findAll() {
        return userPort.findAll();
    }
}

package app.domain.ports;

import java.util.List;
import app.domain.models.User;

public interface UserPort {
    
    boolean existsByDocument(String document);
    
    boolean existsByUsername(String username);
    
    boolean existsByUsernameAndDocumentNot(String username, String document);
    
    void save(User user);
    
    void update(User user);
    
    void deleteByDocument(String document);
    
    User findByDocument(String document);
    
    User findByUsername(String username);
    
    List<User> findAll();
}

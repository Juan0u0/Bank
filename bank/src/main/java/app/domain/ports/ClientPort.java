package app.domain.ports;

import java.util.List;
import app.domain.models.Client;

public interface ClientPort {
    
    boolean existsByDocument(String document);
    
    void save(Client client);
    
    void update(Client client);
    
    void deleteByDocument(String document);
    
    Client findByDocument(String document);
    
    List<Client> findAll();
}

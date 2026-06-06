package app.domain.services;

import app.domain.models.Client;
import app.domain.ports.ClientPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FindClient {
    
    private final ClientPort clientPort;
    
    @Autowired
    public FindClient(ClientPort clientPort) {
        this.clientPort = clientPort;
    }
    
    public Client findByDocument(String document) throws BusinessException {
        Client client = clientPort.findByDocument(document);
        if (client == null) {
            throw new BusinessException("El cliente no existe");
        }
        return client;
    }
    
    public List<Client> findAll() {
        return clientPort.findAll();
    }
}

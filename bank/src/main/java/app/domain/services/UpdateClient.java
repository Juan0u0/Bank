package app.domain.services;

import app.domain.models.Client;
import app.domain.ports.ClientPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateClient {
    
    private final ClientPort clientPort;
    
    @Autowired
    public UpdateClient(ClientPort clientPort) {
        this.clientPort = clientPort;
    }
    
    public void updateClient(Client client) throws BusinessException {
        Client existingClient = clientPort.findByDocument(client.getDocument());
        if (existingClient == null) {
            throw new BusinessException("El cliente no existe");
        }
        
        clientPort.update(client);
    }
}

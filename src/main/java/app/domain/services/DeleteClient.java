package app.domain.services;

import app.domain.ports.ClientPort;
import app.domain.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteClient {
    
    private final ClientPort clientPort;
    
    @Autowired
    public DeleteClient(ClientPort clientPort) {
        this.clientPort = clientPort;
    }
    
    public void deleteClient(String clientDocument) throws BusinessException {
        if (!clientPort.existsByDocument(clientDocument)) {
            throw new BusinessException("El cliente no existe");
        }
        clientPort.deleteByDocument(clientDocument);
    }
}

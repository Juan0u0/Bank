package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    
    boolean existsByDocument(String document);
    
    ClientEntity findByDocument(String document);
    
    void deleteByDocument(String document);
    
    List<ClientEntity> findByClientType(String clientType);
}

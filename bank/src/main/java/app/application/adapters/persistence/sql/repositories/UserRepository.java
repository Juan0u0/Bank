package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    boolean existsByDocument(String document);
    
    boolean existsByUsername(String username);
    
    boolean existsByUsernameAndDocumentNot(String username, String document);
    
    UserEntity findByDocument(String document);
    
    UserEntity findByUsername(String username);
    
    void deleteByDocument(String document);
}

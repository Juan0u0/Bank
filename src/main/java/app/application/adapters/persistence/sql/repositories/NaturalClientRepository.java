package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.NaturalClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturalClientRepository extends JpaRepository<NaturalClientEntity, Long> {

    boolean existsByDocument(String document);

    NaturalClientEntity findByDocument(String document);

    void deleteByDocument(String document);
}

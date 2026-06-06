package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.CompanyClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyClientRepository extends JpaRepository<CompanyClientEntity, Long> {

    boolean existsByNit(String nit);

    CompanyClientEntity findByNit(String nit);

    void deleteByNit(String nit);
}


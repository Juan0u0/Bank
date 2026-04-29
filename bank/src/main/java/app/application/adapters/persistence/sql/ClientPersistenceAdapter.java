package app.application.adapters.persistence.sql;

import app.application.adapters.persistence.sql.entities.ClientEntity;
import app.application.adapters.persistence.sql.repositories.ClientRepository;
import app.domain.models.Client;
import app.domain.models.NaturalPerson;
import app.domain.models.Company;
import app.domain.enums.SistemRole;
import app.domain.ports.ClientPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientPersistenceAdapter implements ClientPort {

    private final ClientRepository repository;

    public ClientPersistenceAdapter(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByDocument(String document) {
        return repository.existsByDocument(document);
    }

    @Override
    public void save(Client client) {
        repository.save(toEntity(client));
    }

    @Override
    public void update(Client client) {
        ClientEntity existing = repository.findByDocument(client.getDocument());
        if (existing != null) {
            existing.setName(client.getName());
            existing.setEmail(client.getEmail());
            existing.setCellPhone(client.getCellPhone());
            existing.setAdress(client.getAdress());
            if (client instanceof NaturalPerson) {
                existing.setBirthDate(((NaturalPerson) client).getBirthDate());
            }
            if (client instanceof Company) {
                Company company = (Company) client;
                existing.setCompanyName(company.getCompanyName());
                existing.setTaxId(company.getTaxId());
                existing.setLegalRepresentative(company.getLegalRepresentative());
                existing.setRole(company.getRole() != null ? company.getRole().name() : null);
            }
            repository.save(existing);
        }
    }

    @Override
    @Transactional
    public void deleteByDocument(String document) {
        repository.deleteByDocument(document);
    }

    @Override
    public Client findByDocument(String document) {
        ClientEntity entity = repository.findByDocument(document);
        return entity != null ? toModel(entity) : null;
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    private ClientEntity toEntity(Client client) {
        ClientEntity entity = new ClientEntity();
        entity.setDocument(client.getDocument());
        entity.setName(client.getName());
        entity.setEmail(client.getEmail());
        entity.setCellPhone(client.getCellPhone());
        entity.setAdress(client.getAdress());

        if (client instanceof NaturalPerson) {
            NaturalPerson naturalPerson = (NaturalPerson) client;
            entity.setClientType("NATURAL_PERSON");
            entity.setBirthDate(naturalPerson.getBirthDate());
        } else if (client instanceof Company) {
            Company company = (Company) client;
            entity.setClientType("COMPANY");
            entity.setCompanyName(company.getCompanyName());
            entity.setTaxId(company.getTaxId());
            entity.setLegalRepresentative(company.getLegalRepresentative());
            entity.setRole(company.getRole() != null ? company.getRole().name() : null);
        }

        return entity;
    }

    private Client toModel(ClientEntity entity) {
        if (entity == null) return null;

        if ("NATURAL_PERSON".equals(entity.getClientType())) {
            NaturalPerson person = new NaturalPerson();
            person.setDocument(entity.getDocument());
            person.setName(entity.getName());
            person.setEmail(entity.getEmail());
            person.setCellPhone(entity.getCellPhone());
            person.setAdress(entity.getAdress());
            person.setBirthDate(entity.getBirthDate());
            return person;
        } else if ("COMPANY".equals(entity.getClientType())) {
            Company company = new Company();
            company.setDocument(entity.getDocument());
            company.setName(entity.getName());
            company.setEmail(entity.getEmail());
            company.setCellPhone(entity.getCellPhone());
            company.setAdress(entity.getAdress());
            company.setCompanyName(entity.getCompanyName());
            company.setTaxId(entity.getTaxId());
            company.setLegalRepresentative(entity.getLegalRepresentative());
            company.setRole(entity.getRole() != null ? SistemRole.valueOf(entity.getRole()) : null);
            return company;
        }

        return null;
    }
}

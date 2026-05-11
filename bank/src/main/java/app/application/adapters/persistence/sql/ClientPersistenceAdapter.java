package app.application.adapters.persistence.sql;

import app.application.adapters.persistence.sql.entities.CompanyClientEntity;
import app.application.adapters.persistence.sql.entities.NaturalClientEntity;
import app.application.adapters.persistence.sql.repositories.CompanyClientRepository;
import app.application.adapters.persistence.sql.repositories.NaturalClientRepository;

import app.domain.models.Client;

import app.domain.ports.ClientPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientPersistenceAdapter implements ClientPort {

    private final NaturalClientRepository naturalRepository;
    private final CompanyClientRepository companyRepository;

    public ClientPersistenceAdapter(NaturalClientRepository naturalRepository, CompanyClientRepository companyRepository) {
        this.naturalRepository = naturalRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public boolean existsByDocument(String document) {
        return naturalRepository.existsByDocument(document) || companyRepository.existsByDocument(document);
    }

    @Override
    public void save(Client client) {
        if (client instanceof app.domain.models.NaturalPerson) {
            naturalRepository.save(toNaturalEntity((app.domain.models.NaturalPerson) client));
        } else if (client instanceof app.domain.models.Company) {
            companyRepository.save(toCompanyEntity((app.domain.models.Company) client));
        }
    }

    @Override
    public void update(Client client) {
        if (client instanceof app.domain.models.NaturalPerson) {
            NaturalClientEntity existing = naturalRepository.findByDocument(client.getDocument());
            if (existing != null) {
                existing.setName(client.getName());
                existing.setEmail(client.getEmail());
                existing.setCellPhone(client.getCellPhone());
                existing.setAdress(client.getAdress());
                existing.setBirthDate(((app.domain.models.NaturalPerson) client).getBirthDate());
                naturalRepository.save(existing);
            }
        } else if (client instanceof app.domain.models.Company) {
            CompanyClientEntity existing = companyRepository.findByDocument(client.getDocument());
            if (existing != null) {
                existing.setName(client.getName());
                existing.setEmail(client.getEmail());
                existing.setCellPhone(client.getCellPhone());
                existing.setAdress(client.getAdress());
                app.domain.models.Company company = (app.domain.models.Company) client;
                existing.setCompanyName(company.getCompanyName());
                existing.setTaxId(company.getTaxId());
                existing.setLegalRepresentative(company.getLegalRepresentative());
                existing.setRole(company.getRole() != null ? company.getRole().name() : null);
                companyRepository.save(existing);
            }
        }
    }

    @Override
    @Transactional
    public void deleteByDocument(String document) {
        naturalRepository.deleteByDocument(document);
        companyRepository.deleteByDocument(document);
    }

    @Override
    public Client findByDocument(String document) {
        NaturalClientEntity natural = naturalRepository.findByDocument(document);
        if (natural != null) return toNaturalModel(natural);
        CompanyClientEntity company = companyRepository.findByDocument(document);
        if (company != null) return toCompanyModel(company);
        return null;
    }

    @Override
    public List<Client> findAll() {
        List<Client> all = naturalRepository.findAll().stream().map(this::toNaturalModel).collect(Collectors.toList());
        all.addAll(companyRepository.findAll().stream().map(this::toCompanyModel).collect(Collectors.toList()));
        return all;
    }
    private NaturalClientEntity toNaturalEntity(app.domain.models.NaturalPerson natural) {
        NaturalClientEntity entity = new NaturalClientEntity();
        entity.setDocument(natural.getDocument());
        entity.setName(natural.getName());
        entity.setEmail(natural.getEmail());
        entity.setCellPhone(natural.getCellPhone());
        entity.setAdress(natural.getAdress());
        entity.setBirthDate(natural.getBirthDate());
        return entity;
    }

    private CompanyClientEntity toCompanyEntity(app.domain.models.Company company) {
        CompanyClientEntity entity = new CompanyClientEntity();
        entity.setDocument(company.getDocument());
        entity.setName(company.getName());
        entity.setEmail(company.getEmail());
        entity.setCellPhone(company.getCellPhone());
        entity.setAdress(company.getAdress());
        entity.setCompanyName(company.getCompanyName());
        entity.setTaxId(company.getTaxId());
        entity.setLegalRepresentative(company.getLegalRepresentative());
        entity.setRole(company.getRole() != null ? company.getRole().name() : null);
        return entity;
    }

    private app.domain.models.NaturalPerson toNaturalModel(NaturalClientEntity entity) {
        app.domain.models.NaturalPerson person = new app.domain.models.NaturalPerson();
        person.setDocument(entity.getDocument());
        person.setName(entity.getName());
        person.setEmail(entity.getEmail());
        person.setCellPhone(entity.getCellPhone());
        person.setAdress(entity.getAdress());
        person.setBirthDate(entity.getBirthDate());
        return person;
    }

    private app.domain.models.Company toCompanyModel(CompanyClientEntity entity) {
        app.domain.models.Company company = new app.domain.models.Company();
        company.setDocument(entity.getDocument());
        company.setName(entity.getName());
        company.setEmail(entity.getEmail());
        company.setCellPhone(entity.getCellPhone());
        company.setAdress(entity.getAdress());
        company.setCompanyName(entity.getCompanyName());
        company.setTaxId(entity.getTaxId());
        company.setLegalRepresentative(entity.getLegalRepresentative());
        company.setRole(entity.getRole() != null ? app.domain.enums.sistemRoles.SistemRole.valueOf(entity.getRole()) : null);
        return company;
    }
}

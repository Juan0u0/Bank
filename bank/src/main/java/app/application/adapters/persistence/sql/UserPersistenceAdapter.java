package app.application.adapters.persistence.sql;

import app.application.adapters.persistence.sql.entities.UserEntity;
import app.application.adapters.persistence.sql.repositories.UserRepository;
import app.domain.models.User;
import app.domain.enums.sistemRoles.SistemRole;
import app.domain.enums.status.UserStatus;
import app.domain.ports.UserPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository repository;

    public UserPersistenceAdapter(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByDocument(String document) {
        return repository.existsByDocument(document);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByUsernameAndDocumentNot(String username, String document) {
        return repository.existsByUsernameAndDocumentNot(username, document);
    }

    @Override
    public void save(User user) {
        repository.save(toEntity(user));
    }

    @Override
    public void update(User user) {
        UserEntity existing = repository.findByDocument(user.getDocument());
        if (existing != null) {
            existing.setName(user.getName());
            existing.setUsername(user.getUsername());
            existing.setPassword(user.getPassword());
            existing.setEmail(user.getEmail());
            existing.setCellPhone(user.getCellPhone());
            existing.setAdress(user.getAdress());
            existing.setRole(user.getRole() != null ? user.getRole().name() : null);
            existing.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
            repository.save(existing);
        }
    }

    @Override
    @Transactional
    public void deleteByDocument(String document) {
        repository.deleteByDocument(document);
    }

    @Override
    public User findByDocument(String document) {
        return toModel(repository.findByDocument(document));
    }

    @Override
    public User findByUsername(String username) {
        return toModel(repository.findByUsername(username));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUserId(user.getUserId());
        entity.setDocument(user.getDocument());
        entity.setName(user.getName());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());
        entity.setCellPhone(user.getCellPhone());
        entity.setAdress(user.getAdress());
        entity.setRole(user.getRole() != null ? user.getRole().name() : null);
        entity.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        return entity;
    }

    private User toModel(UserEntity entity) {
        if (entity == null) return null;
        User user = new User();
        user.setUserId(entity.getUserId());
        user.setDocument(entity.getDocument());
        user.setName(entity.getName());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setEmail(entity.getEmail());
        user.setCellPhone(entity.getCellPhone());
        user.setAdress(entity.getAdress());
        user.setRole(entity.getRole() != null ? SistemRole.valueOf(entity.getRole()) : null);
        user.setStatus(entity.getStatus() != null ? UserStatus.valueOf(entity.getStatus()) : null);
        return user;
    }
}

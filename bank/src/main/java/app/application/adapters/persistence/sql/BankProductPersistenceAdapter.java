package app.application.adapters.persistence.sql;

import app.application.adapters.persistence.sql.entities.BankProductEntity;
import app.application.adapters.persistence.sql.repositories.BankProductRepository;
import app.domain.models.BankProduct;
import app.domain.enums.ProductCategory;
import app.domain.enums.ProductApproval;
import app.domain.ports.BankProductPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankProductPersistenceAdapter implements BankProductPort {

    private final BankProductRepository repository;

    public BankProductPersistenceAdapter(BankProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsById(Long productId) {
        return repository.existsByProductId(productId);
    }

    @Override
    public void save(BankProduct product) {
        repository.save(toEntity(product));
    }

    @Override
    public void update(BankProduct product) {
        BankProductEntity existing = repository.findByProductId(product.getProductId());
        if (existing != null) {
            existing.setProductName(product.getProductName());
            existing.setCategory(product.getCategory() != null ? product.getCategory().name() : null);
            existing.setStatus(product.getStatus() != null ? product.getStatus().name() : null);
            repository.save(existing);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long productId) {
        repository.deleteByProductId(productId);
    }

    @Override
    public BankProduct findById(Long productId) {
        return toModel(repository.findByProductId(productId));
    }

    @Override
    public List<BankProduct> findByCategory(String category) {
        return repository.findByCategory(category)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankProduct> findAll() {
        return repository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    private BankProductEntity toEntity(BankProduct product) {
        BankProductEntity entity = new BankProductEntity();
        entity.setProductId(product.getProductId());
        entity.setProductName(product.getProductName());
        entity.setCategory(product.getCategory() != null ? product.getCategory().name() : null);
        entity.setStatus(product.getStatus() != null ? product.getStatus().name() : null);
        return entity;
    }

    private BankProduct toModel(BankProductEntity entity) {
        if (entity == null) return null;
        BankProduct product = new BankProduct();
        product.setProductId(entity.getProductId());
        product.setProductName(entity.getProductName());
        product.setCategory(entity.getCategory() != null ? ProductCategory.valueOf(entity.getCategory()) : null);
        product.setStatus(entity.getStatus() != null ? ProductApproval.valueOf(entity.getStatus()) : null);
        return product;
    }
}

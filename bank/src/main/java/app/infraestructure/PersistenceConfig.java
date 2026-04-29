package app.infraestructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
        "app.application.adapters.persistence.sql.repositories"
})
@EnableMongoRepositories(basePackages = {
        "app.application.adapters.persistence.mongodb.repositories"
})
public class PersistenceConfig {
    // Configuration class for JPA and MongoDB repositories
    // Enables automatic auditing of entities with @CreatedDate and @LastModifiedDate
    // Configures repository scanning for both SQL and NoSQL persistence layers
}

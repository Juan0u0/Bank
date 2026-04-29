package app.infraestructure;

import app.domain.enums.SistemRole;
import app.domain.enums.UserStatus;
import app.domain.models.User;
import app.domain.ports.UserPort;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapUsers implements ApplicationRunner {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    public BootstrapUsers(UserPort userPort, PasswordEncoder passwordEncoder) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (SistemRole role : SistemRole.values()) {
            String username = role.name();
            if (!userPort.existsByUsername(username)) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole(role);
                user.setStatus(UserStatus.ACTIVE);

                // Campos obligatorios en la entidad
                user.setDocument(username + "_DOC");
                user.setName(username);
                user.setEmail(username.toLowerCase() + "@example.com");

                userPort.save(user);
            }
        }
    }
}

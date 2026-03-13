package app.domain.models;

import app.domain.enums.SistemRole;
import app.domain.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class User extends Person {
    
    private Long userId;
    private String username;
    private String password;
    private SistemRole role;
    private UserStatus status;
}

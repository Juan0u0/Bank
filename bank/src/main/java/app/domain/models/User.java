package app.domain.models;

import app.domain.enums.SistemRole;

public class User extends Person {
    
    private Long userId;
    private String username;
    private String password;
    private SistemRole role;
}

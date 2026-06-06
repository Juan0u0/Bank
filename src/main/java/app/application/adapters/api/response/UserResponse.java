package app.application.adapters.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String document;
    private String name;
    private String username;
    private String role;
    private String status;
    private String email;
    private String cellPhone;
    private String adress;
}

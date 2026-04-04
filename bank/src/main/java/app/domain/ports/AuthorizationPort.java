package app.domain.ports;

import app.domain.enums.SistemRole;

public interface AuthorizationPort {
    
    boolean hasPermission(String userDocument, String action);
    
    boolean roleHasPermission(SistemRole role, String action);
    
    String[] getRolePermissions(SistemRole role);
}

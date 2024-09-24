package es.roomie.user.services;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeycloakService {
    @Value("${keycloak.realm}")
    private String REALM;

    private final Keycloak keycloak;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public UserRepresentation getUserById(String userId) throws NotFoundException {
        return  getUsersResource()
                .get(userId)
                .toRepresentation();
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(REALM).users();
    }
}

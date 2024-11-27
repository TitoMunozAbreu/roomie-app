package es.roomie.user.services;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * KeycloakService is a service class that interacts with the Keycloak server
 * to manage user-related functionalities such as fetching user details.
 * <p>
 * This service uses the Keycloak Admin Client library to communicate with
 * the Keycloak server and perform operations on user resources.
 * </p>
 */
@Service
@Slf4j
public class KeycloakService {
    @Value("${keycloak.realm}")
    private String REALM;

    private final Keycloak keycloak;

    /**
     * Constructor to initialize KeycloakService with the provided Keycloak client.
     *
     * @param keycloak the Keycloak client to be used for API interactions.
     */
    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Retrieves a user representation by the given user ID from Keycloak.
     *
     * @param userId the ID of the user to be retrieved.
     * @return the UserRepresentation of the user with the specified ID.
     * @throws NotFoundException if the user with the specified ID does not exist.
     */
    public UserRepresentation getUserById(String userId) throws NotFoundException {
        return  getUsersResource()
                .get(userId)
                .toRepresentation();
    }

    /**
     * Obtains the UsersResource for the current realm.
     *
     * @return the UsersResource associated with the configured realm.
     */
    private UsersResource getUsersResource() {
        return keycloak.realm(REALM).users();
    }
}

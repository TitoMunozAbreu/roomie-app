package es.roomie.user.controller;

import es.roomie.user.model.User;
import es.roomie.user.services.KeycloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final KeycloakService keycloakService;

    public UserController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") String userId) {
        UserRepresentation userFound = keycloakService.getUserById(userId);
        return User.builder()
                .id(userFound.getId())
                .email(userFound.getEmail())
                .firstname(userFound.getFirstName())
                .lastname(userFound.getLastName())
                .build();
    }
}

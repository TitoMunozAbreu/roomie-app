package es.roomie.user.services;

import es.roomie.user.exceptions.ResourceNotFoundException;
import es.roomie.user.model.User;
import es.roomie.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    public UserService(UserRepository userRepository, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
    }

    public ResponseEntity<User> getUserById(String userId) {
        UserRepresentation userRepresentation = keycloakService.getUserById(userId);

        if (userRepresentation == null) {
            throw new ResourceNotFoundException("User not found");
        }
        log.info("Fetching user detail for {}", userId);
        User userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new ResponseEntity<>(userFound, OK);
    }
}

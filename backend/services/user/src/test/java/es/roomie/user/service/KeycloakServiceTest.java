package es.roomie.user.service;

import es.roomie.user.services.KeycloakService;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource; // Mock para el realm

    @Mock
    private UsersResource usersResource;

    @Mock
    private UserResource userResource;

    @InjectMocks
    private KeycloakService keycloakService;

    @Test
    void getUserById_ReturnsUserRepresentation() {
        // Arrange
        String userId = "12345";
        String realm = "test-realm";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId);
        userRepresentation.setUsername("test-user");

        when(keycloak.realm(realm)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(userId)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);

        // Act
        UserRepresentation result = keycloakService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test-user", result.getUsername());
        verify(usersResource).get(userId);
    }

    @Test
    void getUserById_ThrowsNotFoundException_WhenUserNotFound() {
        // Arrange
        String userId = "non-existent";
        String realm = "test-realm";

        lenient().when(keycloak.realm(realm)).thenReturn(realmResource);
        lenient().when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(userId)).thenThrow(new NotFoundException("User not found"));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> keycloakService.getUserById(userId));
    }

    @Test
    void testSomething() {
        ReflectionTestUtils.setField(keycloakService, "REALM", "test-realm");
        // continuar con las pruebas
    }
}

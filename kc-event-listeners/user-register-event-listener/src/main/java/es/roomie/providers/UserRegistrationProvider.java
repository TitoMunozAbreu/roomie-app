package es.roomie.providers;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.keycloak.events.EventType.REGISTER;

/**
 * UserRegistrationProvider is an implementation of the EventListenerProvider interface
 * that listens for user registration events and communicates with a backend service
 * to register the user information.
 */
public class UserRegistrationProvider implements EventListenerProvider {

    private KeycloakSession session;
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationProvider.class);

    /**
     * Constructs a new UserRegistrationProvider with the specified Keycloak session.
     *
     * @param session the Keycloak session
     */
    public UserRegistrationProvider(KeycloakSession session) {
        this.session = session;
    }

    /**
     * Handles the incoming events. Specifically, it processes user registration events.
     *
     * @param event the event to process
     */
    @Override
    public void onEvent(Event event) {
        if (event.getType() == REGISTER) {
            logger.info("Loading registration event from user {}", event.getUserId());
            sendUserCreatedEventToBackend(event);
        }
    }

    /**
     * Handles admin events. Currently, this implementation does not process any admin events.
     *
     * @param adminEvent the admin event to process
     * @param b         additional flag (not used)
     */
    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    /**
     * Cleans up any resources used by the provider. Currently, this implementation does nothing.
     */
    @Override
    public void close() {
    }

    /**
     * Sends the user creation event to the backend service.
     *
     * @param event the event containing user information
     */
    private void sendUserCreatedEventToBackend(Event event) {
        try {
            String HOST_BACKEND = System.getenv("HOST_BACKEND");
            // URL of the backend to which the event will be sent
            URL url = new URL("%s/api/v1/users".formatted(HOST_BACKEND));

            // Create HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);  // Permitir enviar datos

            // Create request body (JSON with user details)
            String jsonInputString = "{ \"userId\": \"" + event.getUserId() + "\", \"email\": \"" + event.getDetails().get("email") + "\"}";

            // Send data in the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                logger.info("Request to register user: {}", jsonInputString);
            } else {
                logger.error("Error sending user to the backend: " + responseCode);
            }
        } catch (Exception e) {
            logger.error("Error sending user-created event: ", e);
        }
    }
}

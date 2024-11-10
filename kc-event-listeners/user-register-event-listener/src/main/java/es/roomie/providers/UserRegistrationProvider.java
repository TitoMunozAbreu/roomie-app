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


public class UserRegistrationProvider implements EventListenerProvider {

    private KeycloakSession session;
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationProvider.class);

    public UserRegistrationProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() == REGISTER) {
            logger.info("Loading registration event from user {}", event.getUserId());
            sendUserCreatedEventToBackend(event);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {
    }

    private void sendUserCreatedEventToBackend(Event event) {
        try {
            String HOST_BACKEND = System.getenv("HOST_BACKEND");
            // URL del backend a la que quieres enviar el evento
            URL url = new URL("%s/api/v1/users".formatted(HOST_BACKEND));

            // Crear conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);  // Permitir enviar datos

            // Crear el cuerpo de la solicitud (JSON con los detalles del usuario)
            String jsonInputString = "{ \"userId\": \"" + event.getUserId() + "\", \"email\": \"" + event.getDetails().get("email") + "\"}";

            // Enviar datos en la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Obtener la respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                logger.info("Solicitud de registrar usuario: {}", jsonInputString);
            } else {
                logger.error("Error al enviar usuario al backend: " + responseCode);
            }
        } catch (Exception e) {
            logger.error("Error al enviar evento de usuario creado: ", e);
        }
    }
}

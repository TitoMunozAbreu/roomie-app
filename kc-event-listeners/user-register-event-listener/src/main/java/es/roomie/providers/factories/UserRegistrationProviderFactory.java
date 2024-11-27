package es.roomie.providers.factories;

import es.roomie.providers.UserRegistrationProvider;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * Factory class for creating instances of the UserRegistrationProvider.
 * Implements the EventListenerProviderFactory interface from Keycloak.
 */
public class UserRegistrationProviderFactory implements EventListenerProviderFactory {

    private static final String PROVIDER_ID = "custom-user-registration";

    /**
     * Returns the unique identifier for this EventListenerProviderFactory.
     *
     * @return the provider ID as a String
     */
    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    /**
     * Creates a new instance of UserRegistrationProvider.
     *
     * @param session the Keycloak session to be used by the provider
     * @return a new UserRegistrationProvider instance
     */
    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new UserRegistrationProvider(session);
    }

    /**
     * Initializes the factory with the given configuration scope.
     *
     * @param scope the configuration scope containing initialization parameters
     */
    @Override
    public void init(Config.Scope scope) {

    }

    /**
     * Post-initialization logic that can be executed after the factory has been initialized.
     *
     * @param keycloakSessionFactory the Keycloak session factory
     */
    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    /**
     * Closes the factory and releases any resources held.
     */
    @Override
    public void close() {
    }
}

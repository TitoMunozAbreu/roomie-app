package es.roomie.household.config;

import es.roomie.household.service.client.feign.TokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Feign Clients.
 * This class provides a RequestInterceptor bean
 * that adds an Authorization header with a JWT token
 * to outgoing requests made by Feign clients.
 */
@Configuration
public class FeignClientConfig {
    private final TokenService tokenService;

    /**
     * Constructor for FeignClientConfig.
     * @param tokenService An instance of TokenService to be used for retrieving JWT tokens.
     */
    public FeignClientConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Provides a RequestInterceptor bean.
     * @return A RequestInterceptor that adds an Authorization header with a JWT token
     *         to all requests made by Feign clients.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String jwt = tokenService.getToken();
            requestTemplate.header("Authorization", "Bearer " + jwt);
        };
    }
}
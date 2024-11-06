package es.roomie.household.config;

import es.roomie.household.service.client.feign.TokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignClientConfig {
    private final TokenService tokenService;

    public FeignClientConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String jwt = tokenService.getToken();
            requestTemplate.header("Authorization", "Bearer " + jwt);
        };
    }
}
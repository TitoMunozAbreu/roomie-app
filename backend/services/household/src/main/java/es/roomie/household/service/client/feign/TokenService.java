package es.roomie.household.service.client.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * Represents a service for managing tokens.
 * This class is responsible for handling the token
 * associated with a user or a session in the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class TokenService {
    private String token;
}
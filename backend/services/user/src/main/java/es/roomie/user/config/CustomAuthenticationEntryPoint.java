package es.roomie.user.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomAuthenticationEntryPoint is a class that implements the AuthenticationEntryPoint interface.
 * It is used to handle unauthorized access attempts in a Spring Security context.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * This method is called when an authentication exception occurs.
     *
     * @param request       the HttpServletRequest that caused the authentication exception
     * @param response      the HttpServletResponse to send the error response
     * @param authException the exception that triggered the invocation of this method
     * @throws IOException if an input or output exception occurs during the handling of the response
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"You do not have permissions to access this resource.\"}");
    }
}
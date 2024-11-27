package es.roomie.user.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomAccessDeniedHandler implements the AccessDeniedHandler interface to provide
 * a custom response when access is denied.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handles access denied exceptions by sending a JSON response with a 403 status code.
     *
     * @param request                 The HttpServletRequest that resulted in an AccessDeniedException
     * @param response                The HttpServletResponse to send the response to the client
     * @param accessDeniedException    The exception that was thrown when access was denied
     * @throws IOException            If an input or output exception occurs while handling the request
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"\"You do not have sufficient permissions to access this resource..\"}");
    }
}
package es.roomie.user.exceptions;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * GlobalExceptionHandler is a REST controller advice that handles exceptions thrown
 * by the application and returns appropriate HTTP responses.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GloblalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns a 404 NOT FOUND response.
     *
     * @param ex the exception thrown when a resource is not found
     * @return ResponseEntity containing the error message and 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
    }

    /**
     * Handles NotFoundException and returns a 404 NOT FOUND response with a default message.
     *
     * @param ex the NotFoundException thrown
     * @return ResponseEntity containing a default message and 404 status
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>("User not found", NOT_FOUND);
    }

    /**
     * Handles UnAuthorizeUserException and returns a 401 UNAUTHORIZED response.
     *
     * @param ex the exception thrown when a user is not authorized
     * @return ResponseEntity containing the error message and 401 status
     */
    @ExceptionHandler(UnAuthorizeUserException.class)
    public ResponseEntity<?> handleUnAuthorizeUserException(UnAuthorizeUserException ex) {
        return new ResponseEntity<>(ex.getMessage(), UNAUTHORIZED);
    }
}

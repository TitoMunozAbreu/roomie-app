package es.roomie.task.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Global exception handler for the application.
 * This class handles exceptions that occur within the application and
 * returns appropriate HTTP responses.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException thrown by the application.
     *
     * @param ex the ResourceNotFoundException that was thrown
     * @return a ResponseEntity containing the exception message and a
     *         NOT_FOUND (404) HTTP status code
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
    }
}

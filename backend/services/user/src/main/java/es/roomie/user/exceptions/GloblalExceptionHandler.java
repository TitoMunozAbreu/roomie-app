package es.roomie.user.exceptions;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.*;

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

    /**
     * Handles MethodArgumentNotValidException thrown when a method argument fails validation.
     *
     * @param ex the MethodArgumentNotValidException that was thrown
     * @return a ResponseEntity containing a map of field error messages and a BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HashMap<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }
}

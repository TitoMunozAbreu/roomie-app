package es.roomie.household.exceptions;

import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.*;

/**
 * GlobalExceptionHandler is a class that handles exceptions
 * thrown by controllers in the application.
 * It uses Spring's @RestControllerAdvice to provide centralized
 * exception handling across all controllers.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GloblalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns a
     * ResponseEntity with a NOT_FOUND status.
     *
     * @param ex the ResourceNotFoundException that was thrown
     * @return a ResponseEntity containing the exception message and a NOT_FOUND status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
    }

    /**
     * Handles ForbiddenUserException and returns a
     * ResponseEntity with a FORBIDDEN status.
     *
     * @param ex the ForbiddenUserException that was thrown
     * @return a ResponseEntity containing the exception message and a FORBIDDEN status
     */
    @ExceptionHandler(ForbiddenUserException.class)
    public ResponseEntity<?> handlerForbiddenUserException(ForbiddenUserException ex) {
        return new ResponseEntity<>(ex.getMessage(), FORBIDDEN);
    }

    /**
     * Handles BadRequestException and returns a
     * ResponseEntity with a BAD_REQUEST status.
     *
     * @param ex the BadRequestException that was thrown
     * @return a ResponseEntity containing the exception message and a BAD_REQUEST status
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handlerBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
    }

    /**
     * Handles MissingServletRequestParameterException and returns a
     * ResponseEntity with a BAD_REQUEST status.
     *
     * @param ex the MissingServletRequestParameterException that was thrown
     * @return a ResponseEntity containing the exception message and a BAD_REQUEST status
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handlerBadRequestException(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
    }

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

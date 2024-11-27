package es.roomie.user.exceptions;

/**
 * Exception thrown when a requested resource cannot be found.
 * This is a runtime exception, so it does not need to be declared
 * in a method's throws clause.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

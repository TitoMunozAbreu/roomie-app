package es.roomie.household.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * This class extends RuntimeException and provides a constructor
 * that accepts a message to be passed to the super class.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

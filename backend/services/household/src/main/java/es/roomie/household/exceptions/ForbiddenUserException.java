package es.roomie.household.exceptions;

/**
 * Exception thrown when a user is forbidden from performing an action.
 * This is a custom exception that extends the RuntimeException.
 */
public class ForbiddenUserException extends RuntimeException {
    public ForbiddenUserException(String message) {super(message);}
}

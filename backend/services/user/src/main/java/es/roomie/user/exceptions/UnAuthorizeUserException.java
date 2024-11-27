package es.roomie.user.exceptions;

/**
 * Exception thrown when a user is not authorized to perform a certain action.
 * This class extends the RuntimeException, allowing it to be thrown
 * during the normal operation of the Java Virtual Machine.
 */
public class UnAuthorizeUserException extends RuntimeException {
    public UnAuthorizeUserException(String message) {
        super(message);
    }
}

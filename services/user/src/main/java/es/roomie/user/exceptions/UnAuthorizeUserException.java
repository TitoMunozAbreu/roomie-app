package es.roomie.user.exceptions;

public class UnAuthorizeUserException extends RuntimeException {
    public UnAuthorizeUserException(String message) {
        super(message);
    }
}

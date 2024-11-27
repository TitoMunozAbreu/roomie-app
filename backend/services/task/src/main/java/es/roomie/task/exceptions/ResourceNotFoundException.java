package es.roomie.task.exceptions;
/**
 * Custom exception class to handle scenarios where a requested resource is not found.
 * This class extends RuntimeException, allowing it to be thrown during the program's execution
 * without being explicitly declared in a method's throws clause.
 */
 public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

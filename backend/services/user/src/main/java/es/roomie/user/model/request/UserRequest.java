package es.roomie.user.model.request;

/**
 * Represents a request for user information.
 * This record holds the user ID and email address of a user.
 */
public record UserRequest(String userId, String email) {}

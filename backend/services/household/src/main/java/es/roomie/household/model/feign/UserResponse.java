package es.roomie.household.model.feign;

/**
 * Represents a response containing user information.
 * This record holds the details of a user including their ID, first name, last name, and email address.
 *
 * @param id the unique identifier of the user
 * @param firstName the first name of the user
 * @param lastName the last name of the user
 * @param email the email address of the user
 */
public record UserResponse(String id, String firstName, String lastName, String email) {}

package es.roomie.household.model.resquest;

/**
 * A record that represents a request to create or update a household.
 * This class encapsulates the details necessary for identifying a household
 * and its associated user.
 *
 * @param householdName the name of the household
 * @param userId the unique identifier of the user associated with the household
 * @param email the email address of the user associated with the household
 */
public record HouseholdRequest(String householdName, String userId, String email) {}

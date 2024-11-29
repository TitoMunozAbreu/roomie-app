package es.roomie.user.model.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a request for user information.
 * This record holds the user ID and email address of a user.
 */
public record UserRequest(@NotBlank String userId,
                          @NotBlank String email) {}

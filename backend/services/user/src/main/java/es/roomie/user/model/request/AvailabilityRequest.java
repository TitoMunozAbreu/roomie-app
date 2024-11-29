package es.roomie.user.model.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Represents a request for availability.
 * This record holds information about a specific day and the hours available on that day.
 */
public record AvailabilityRequest (
        @NotBlank String day,
        List<String> hours
){}
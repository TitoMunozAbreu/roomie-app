package es.roomie.user.model.request;

import java.util.List;

/**
 * Represents a request for availability.
 * This record holds information about a specific day and the hours available on that day.
 */
public record AvailabilityRequest (
        String day,
        List<String> hours
){}

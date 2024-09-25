package es.roomie.user.model.request;

import java.util.List;

public record AvailabilityRequest (
        String day,
        List<String> hours
){}

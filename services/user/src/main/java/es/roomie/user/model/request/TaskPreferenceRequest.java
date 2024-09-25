package es.roomie.user.model.request;

public record TaskPreferenceRequest(
        String taskName,
        String preference
) {}

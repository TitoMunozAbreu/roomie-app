package es.roomie.user.model.request;

/**
 * Represents a request for a task preference.
 * This record holds the details of a task's name along with the user's preference
 * regarding that task.
 *
 * @param taskName the name of the task
 * @param preference the user's preference for the task
 */
public record TaskPreferenceRequest(
        String taskName,
        String preference
) {}

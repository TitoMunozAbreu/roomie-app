package es.roomie.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a user's task preference.
 * It contains the name of the task and the user's preference for that task.
 */
 @Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPreference {
    private String taskName;
    private String preference;
}

package es.roomie.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the history of a task.
 * Contains information about the task ID and the date it was completed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskHistory {
    private String taskId;
    private String completedDate;
}

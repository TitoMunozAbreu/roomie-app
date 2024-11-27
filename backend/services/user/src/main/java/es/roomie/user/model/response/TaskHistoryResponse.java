package es.roomie.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response model for task history.
 * This class encapsulates the details of a task's history including
 * the task ID and the date it was completed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskHistoryResponse {
    private String taskId;
    private String completedDate;
}

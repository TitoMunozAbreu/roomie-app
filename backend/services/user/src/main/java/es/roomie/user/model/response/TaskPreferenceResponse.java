package es.roomie.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for task preferences response.
 * It contains the name of the task and the user's preference
 * regarding that task.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPreferenceResponse {
    private String taskName;
    private String preference;
}

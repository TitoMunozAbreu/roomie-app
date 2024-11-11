package es.roomie.task.model.response;

import es.roomie.task.config.enums.Category;
import es.roomie.task.config.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    private String id;
    private String householdId;
    private String title;
    private String description;
    private Category category;
    private int estimatedDuration;
    private String assignedTo;
    private LocalDateTime dueDate;
    private Status status;
}

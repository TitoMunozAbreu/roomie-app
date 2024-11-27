package es.roomie.household.model.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a response for a task in a household management system.
 * This class contains information regarding the task such as its ID,
 * the household it belongs to, who created it, its title, description,
 * category, estimated duration, assigned user, due date, and its status.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    private String id;
    private String householdId;
    private String createdBy;
    private String title;
    private String description;
    private String category;
    private int estimatedDuration;
    private String assignedTo;
    private LocalDateTime dueDate;
    private String status;
}

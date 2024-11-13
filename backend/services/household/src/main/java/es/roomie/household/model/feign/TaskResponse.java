package es.roomie.household.model.feign;

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
    private String createdBy;
    private String title;
    private String description;
    private String category;
    private int estimatedDuration;
    private String assignedTo;
    private LocalDateTime dueDate;
    private String status;
}

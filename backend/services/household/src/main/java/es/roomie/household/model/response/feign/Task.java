package es.roomie.household.model.response.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private String taskId;
    private String taskName;
    private String assignedTo;
    private LocalDate assignmentDate;
    private String dueDate;
    private String status;
}

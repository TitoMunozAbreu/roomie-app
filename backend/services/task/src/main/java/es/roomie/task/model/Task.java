package es.roomie.task.model;

import es.roomie.task.config.enums.Category;
import es.roomie.task.config.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Task {
    @Id private String id;
    private String householdId;
    private String title;
    private String description;
    private Category category;
    private int estimatedDuration;
    private String assignedTo;
    private LocalDateTime creationDate;
    private LocalDateTime dueDate;
    private Status status;
    private Statistics statistics;
}

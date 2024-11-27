package es.roomie.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the statistics of tasks in the system.
 * This class holds the total number of tasks and the number of completed tasks.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Statistics {
    private int totalTask;
    private int completedTasks;

    public void incrementTotalTask() {
        ++this.totalTask;
    }

    public void incrementCompletedTasks() {
        ++this.completedTasks;
    }
}

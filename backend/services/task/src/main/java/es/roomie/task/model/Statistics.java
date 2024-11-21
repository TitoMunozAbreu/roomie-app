package es.roomie.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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

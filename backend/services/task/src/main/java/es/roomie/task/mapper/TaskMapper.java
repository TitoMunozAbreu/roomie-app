package es.roomie.task.mapper;

import es.roomie.task.model.Statistics;
import es.roomie.task.model.Task;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task mapToTask(TaskResquest task);

    TaskResponse mapToTaskResponse(Task task);

    @AfterMapping()
    default void mapToTask(@MappingTarget Task task, TaskResquest taskResquest) {
        //set UUID
        task.setId(UUID.randomUUID().toString());

        //set creation date
        task.setCreationDate(LocalDateTime.now());

        //set statistics
        task.setStatistics(Statistics.builder()
                .totalTask(0)
                .completedTasks(0)
                .build());
    }
}

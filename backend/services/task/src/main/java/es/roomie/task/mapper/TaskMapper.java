package es.roomie.task.mapper;

import es.roomie.task.model.Statistics;
import es.roomie.task.model.Task;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE)
public interface TaskMapper {

    Task mapToTask(TaskResquest task);

    TaskResponse mapToTaskResponse(Task task);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT,
                     elementTargetType = TaskMapper.class)
    List<TaskResponse> mapToTasksResponse(List<Task> tasks);

    default Task updateTaskFromRequest(TaskResquest taskResquest, Task taskFound) {
        Optional.ofNullable(taskResquest.householdId())
                .filter(householdId -> !Objects.equals(householdId, taskFound.getHouseholdId()))
                .ifPresent(taskFound::setHouseholdId);

        Optional.ofNullable(taskResquest.title())
                .filter(title -> !Objects.equals(title, taskFound.getTitle()))
                .ifPresent(taskFound::setTitle);

        Optional.ofNullable(taskResquest.description())
                .filter(description -> !Objects.equals(description, taskFound.getDescription()))
                .ifPresent(taskFound::setDescription);

        Optional.ofNullable(taskResquest.category())
                .filter(category -> !Objects.equals(category, taskFound.getCategory()))
                .ifPresent(taskFound::setCategory);

        Optional.of(taskResquest.estimatedDuration())
                .filter(estimatedDuration -> !Objects.equals(estimatedDuration, taskFound.getEstimatedDuration()))
                .ifPresent(taskFound::setEstimatedDuration);

        Optional.ofNullable(taskResquest.assignedTo())
                .filter(assignedTo -> !Objects.equals(assignedTo, taskFound.getAssignedTo()))
                .ifPresent(taskFound::setAssignedTo);

        Optional.ofNullable(taskResquest.dueDate())
                .filter(dueDate -> !Objects.equals(dueDate, taskFound.getDueDate()))
                .ifPresent(taskFound::setDueDate);

        Optional.ofNullable(taskResquest.status())
                .filter(status -> !Objects.equals(status, taskFound.getStatus()))
                .ifPresent(taskFound::setStatus);

        return taskFound;
    }

    @AfterMapping()
    default void mapToTask(@MappingTarget Task task, TaskResquest taskResquest) {
        //set UUID
        task.setId(UUID.randomUUID().toString());

        //set creation date
        task.setCreatedAt(LocalDateTime.now());

        //set statistics
        task.setStatistics(Statistics.builder()
                .totalTask(0)
                .completedTasks(0)
                .build());
    }
}

package es.roomie.task.service;

import es.roomie.task.config.enums.Status;
import es.roomie.task.exceptions.ResourceNotFoundException;
import es.roomie.task.kafka.NotificationMessage;
import es.roomie.task.kafka.TaskProducer;
import es.roomie.task.mapper.TaskMapper;
import es.roomie.task.model.Task;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static es.roomie.task.config.enums.Status.Completed;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

/**
 * Service class for managing tasks within the application.
 * This class contains methods to create, update, delete,
 * and retrieve tasks for a specified household.
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskProducer taskProducer;


    /**
     * Retrieves a list of tasks associated with the specified household IDs.
     *
     * @param householdIds List of household IDs to filter tasks
     * @return ResponseEntity containing a list of TaskResponse objects
     * @throws ResourceNotFoundException if no tasks are found for the given IDs
     */
    public ResponseEntity<List<TaskResponse>> getTasksByHouseholdIdIn(List<String> householdIds) {
        log.info("Fetch tasks");
        List<Task> tasks = taskRepository.findByHouseholdIdIn(householdIds);

        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("No tasks found with ids: " + householdIds);
        }

        return new ResponseEntity<>(taskMapper.mapToTasksResponse(tasks), OK);
    }

    /**
     * Creates a new task based on the provided request data.
     *
     * @param taskResquest The request data for creating a task
     * @return ResponseEntity containing the created TaskResponse object
     */
    public ResponseEntity<TaskResponse> createTask(TaskResquest taskResquest) {
        Task task = taskMapper.mapToTask(taskResquest);
        incrementTotalTask(task);

        log.info("Insert task");
        taskRepository.insert(task);

        taskProducer.sendTaskNotification(
                new NotificationMessage(
                        "New Task Assigned",
                        String.format("A new task has been created and assigned to '%s'. Please check the details in your Roomie app.",taskResquest.assignedTo()),
                        taskResquest.assignedTo(),
                        taskResquest.title(),
                        taskResquest.dueDate().toLocalDate().toString()
                )
        );

        return new ResponseEntity<>(taskMapper.mapToTaskResponse(task), OK);
    }

    /**
     * Updates an existing task identified by the task ID.
     *
     * @param taskId The ID of the task to be updated
     * @param taskResquest The request data for updating the task
     * @return ResponseEntity containing the updated TaskResponse object
     * @throws ResourceNotFoundException if the task is not found
     */
    public ResponseEntity<TaskResponse> updateTask(String taskId, TaskResquest taskResquest) {
        Task taskFound = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found."));

        taskMapper.updateTaskFromRequest(taskResquest, taskFound);

        if (taskFound.getStatus() == Completed) {
            incrementCompletedTask(taskFound);

            taskProducer.sendTaskNotification(
                    new NotificationMessage(
                            "Task Completed",
                            "Congratulations! The task you were assigned has been successfully marked as completed.",
                            taskResquest.assignedTo(),
                            taskResquest.title(),
                            taskResquest.dueDate().toLocalDate().toString()
                    )
            );
        }

        log.info("Update task");
        taskRepository.save(taskFound);

        return new ResponseEntity<>(taskMapper.mapToTaskResponse(taskFound), OK);
    }

    /**
     * Updates the status of a task identified by the task ID.
     *
     * @param taskId The ID of the task to update the status
     * @param status The new status to be set for the task
     * @return ResponseEntity containing the updated status of the task
     * @throws ResourceNotFoundException if the task is not found
     */
    public ResponseEntity<?> updateStatus(String taskId, Status status) {
        Task taskFound = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found."));

        boolean isCompletedStatus = status.equals(Completed) && taskFound.getStatus() != Completed;

        if (taskFound.getStatus() != status) {
            taskFound.setStatus(status);

            if (isCompletedStatus) {
                incrementCompletedTask(taskFound);
                taskProducer.sendTaskNotification(
                        new NotificationMessage(
                                "Task Completed",
                                "Congratulations! The task you were assigned has been successfully marked as completed.",
                                taskFound.getAssignedTo(),
                                taskFound.getTitle(),
                                taskFound.getDueDate().toLocalDate().toString()
                        )
                );
            }

            log.info("Update status");
            taskRepository.save(taskFound);
        }

        return new ResponseEntity<>(of("status", taskFound.getStatus()), OK);
    }

    /**
     * Deletes a task identified by the task ID.
     *
     * @param taskId The ID of the task to be deleted
     * @return ResponseEntity confirming the deletion of the task
     * @throws ResourceNotFoundException if the task is not found
     */
    public ResponseEntity<?> deleteTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found."));

        log.info("Delete task");
        taskRepository.delete(task);

        taskProducer.sendTaskNotification(
                new NotificationMessage(
                        "Task Removed",
                        "A task assigned to you has been removed. No further action is required.",
                        task.getAssignedTo(),
                        task.getTitle(),
                        task.getDueDate().toLocalDate().toString()
                )
        );

        return new ResponseEntity<>("Deleted task", OK);
    }

    /**
     * Increments the total task count for a given task.
     *
     * @param task The task for which to increment the total count
     * @return The updated task object
     */
    private Task incrementTotalTask(Task task) {
        task.getStatistics().incrementTotalTask();
        return task;
    }

    /**
     * Increments the completed task count for a given task.
     *
     * @param task The task for which to increment the completed count
     */
    private void incrementCompletedTask(Task task) {
        task.getStatistics().incrementCompletedTasks();
    }

    /**
     * Deletes all tasks associated with a specified household ID.
     *
     * @param householdId The ID of the household whose tasks will be deleted
     * @return ResponseEntity confirming the deletion of all tasks
     */
    public ResponseEntity<?> deleteAllTasks(String householdId) {
        log.info("Delete all tasks");
        taskRepository.deleteByHouseholdId(householdId);

        return new ResponseEntity<>("Deleted all tasks", OK);
    }
}
package es.roomie.task.controller;

import es.roomie.task.config.enums.Status;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TaskController is a REST controller that handles HTTP requests related
 * to task management. It provides endpoints to create, retrieve, update,
 * and delete tasks associated with households.
 */
@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructs a TaskController with the specified TaskService.
     * @param taskService The service that provides task-related operations.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Retrieves a list of tasks associated with the given household IDs.
     *
     * @param householdIds A list of household IDs to filter tasks.
     * @return A ResponseEntity containing a list of TaskResponse objects.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasksByHouseholdIdIn(@RequestParam List<String> householdIds) {
        return taskService.getTasksByHouseholdIdIn(householdIds);
    }

    /**
     * Creates a new task based on the provided task request data.
     *
     * @param taskResquest The request object containing task data.
     * @return A ResponseEntity containing the created TaskResponse object.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody
                                                   @Valid TaskResquest taskResquest) {
        return taskService.createTask(taskResquest);
    }

    /**
     * Updates an existing task identified by the task ID.
     *
     * @param taskId The ID of the task to be updated.
     * @param taskResquest The request object containing the updated task data.
     * @return A ResponseEntity containing the updated TaskResponse object.
     */
    @PutMapping("{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String taskId,
                                                   @RequestBody
                                                   @Valid TaskResquest taskResquest) {
        return taskService.updateTask(taskId, taskResquest);
    }

    /**
     * Updates the status of an existing task identified by the task ID.
     *
     * @param taskId The ID of the task whose status is to be updated.
     * @param status The new status to be set for the task.
     * @return A ResponseEntity containing the result of the status update operation.
     */
    @PatchMapping("{taskId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String taskId,
                                         @RequestParam("status") Status status) {
        return taskService.updateStatus(taskId, status);
    }

    /**
     * Deletes an existing task identified by the task ID.
     *
     * @param taskId The ID of the task to be deleted.
     * @return A ResponseEntity containing the result of the delete operation.
     */
    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId) {
        return taskService.deleteTask(taskId);
    }

    /**
     * Deletes all tasks associated with a specified household ID.
     *
     * @param householdId The ID of the household whose tasks are to be deleted.
     * @return A ResponseEntity containing the result of the delete operation.
     */
    @DeleteMapping()
    public ResponseEntity<?> deleteAllTasks(@RequestParam String householdId) {
        return  taskService.deleteAllTasks(householdId);
    }

}

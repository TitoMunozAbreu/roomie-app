package es.roomie.task.controller;

import es.roomie.task.config.enums.Status;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskResquest taskResquest) {
        return taskService.createTask(taskResquest);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String taskId,
                                                   @RequestBody TaskResquest taskResquest) {
        return taskService.updateTask(taskId, taskResquest);
    }

    @PatchMapping("{taskId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String taskId,
                                                         @RequestParam("status") Status status) {
        return taskService.updateStatus(taskId, status);
    }

}

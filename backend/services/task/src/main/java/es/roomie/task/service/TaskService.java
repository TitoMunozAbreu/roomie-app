package es.roomie.task.service;

import es.roomie.task.config.enums.Status;
import es.roomie.task.exceptions.ResourceNotFoundException;
import es.roomie.task.mapper.TaskMapper;
import es.roomie.task.model.Task;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static es.roomie.task.config.enums.Status.Completed;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public ResponseEntity<List<TaskResponse>> getTasksByHouseholdIdIn(List<String> householdIds) {
        log.info("Fetch tasks");
        List<Task> tasks = taskRepository.findByHouseholdIdIn(householdIds);

        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("No tasks found with ids: " + householdIds);
        }

        return new ResponseEntity<>(taskMapper.mapToTasksResponse(tasks), OK);
    }

    public ResponseEntity<TaskResponse> createTask(TaskResquest taskResquest) {
        Task task = taskMapper.mapToTask(taskResquest);
        incrementTotalTask(task);

        log.info("Insert task");
        taskRepository.insert(task);

        return new ResponseEntity<>(taskMapper.mapToTaskResponse(task), OK);
    }

    public ResponseEntity<TaskResponse> updateTask(String taskId, TaskResquest taskResquest) {
        Task taskFound = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found."));

        taskMapper.updateTaskFromRequest(taskResquest, taskFound);

        if (taskFound.getStatus() == Completed) {
            incrementCompletedTask(taskFound);
        }

        log.info("Update task");
        taskRepository.save(taskFound);

        return new ResponseEntity<>(taskMapper.mapToTaskResponse(taskFound), OK);
    }

    public ResponseEntity<?> updateStatus(String taskId, Status status) {
        Task taskFound = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found."));

        boolean isCompletedStatus = status.equals(Completed) && taskFound.getStatus() != Completed;

        if (taskFound.getStatus() != status) {
            taskFound.setStatus(status);

            if (isCompletedStatus) {
                incrementCompletedTask(taskFound);
            }

            log.info("Update status");
            taskRepository.save(taskFound);
        }

        return new ResponseEntity<>(of("status", taskFound.getStatus()), OK);
    }

    public ResponseEntity<?> deleteTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No task found."));

        log.info("Delete task");
        taskRepository.delete(task);

        return new ResponseEntity<>("Deleted task", OK);
    }

    private Task incrementTotalTask(Task task) {
        task.getStatistics().incrementTotalTask();
        return task;
    }

    private void incrementCompletedTask(Task task) {
        task.getStatistics().incrementCompletedTasks();
    }

    public ResponseEntity<?> deleteAllTasks(String householdId) {
        log.info("Delete all tasks");
        taskRepository.deleteByHouseholdId(householdId);

        return new ResponseEntity<>("Deleted all tasks", OK);
    }
}
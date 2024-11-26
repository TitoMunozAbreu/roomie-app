package es.roomie.task.service;

import es.roomie.task.config.enums.Category;
import es.roomie.task.config.enums.Status;
import es.roomie.task.exceptions.ResourceNotFoundException;
import es.roomie.task.kafka.NotificationMessage;
import es.roomie.task.kafka.TaskProducer;
import es.roomie.task.mapper.TaskMapper;
import es.roomie.task.model.Statistics;
import es.roomie.task.model.Task;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskProducer taskProducer;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskResquest taskRequest;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setStatistics(new Statistics());
        task.setDueDate(LocalDateTime.now());

        taskRequest = new TaskResquest("1", "household-1", "User", "Test Task",
                "Description of the task", Category.Cleaning, 60, "User",
                LocalDateTime.now(), Status.Pending);
        taskResponse = new TaskResponse("1", "household-1", "User", "Test Task",
                "Description of the task", "General", 60, "User",
                LocalDateTime.now(), Status.Pending.toString());
    }

    @Test
    void getTasksByHouseholdIdIn_ShouldReturnTasks_WhenTasksExist() {
        List<String> householdIds = List.of("household1");
        when(taskRepository.findByHouseholdIdIn(householdIds)).thenReturn(List.of(task));
        when(taskMapper.mapToTasksResponse(any())).thenReturn(List.of(taskResponse));

        ResponseEntity<List<TaskResponse>> response = taskService.getTasksByHouseholdIdIn(householdIds);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).containsExactly(taskResponse);
    }

    @Test
    void getTasksByHouseholdIdIn_ShouldThrowException_WhenNoTasksFound() {
        List<String> householdIds = List.of("household2");
        when(taskRepository.findByHouseholdIdIn(householdIds)).thenReturn(Collections.emptyList());

        ResourceNotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTasksByHouseholdIdIn(householdIds));

        assertThat(thrown.getMessage()).isEqualTo("No tasks found with ids: " + householdIds);
    }

    @Test
    void createTask_ShouldCreateTaskAndSendNotification() {
        when(taskMapper.mapToTask(taskRequest)).thenReturn(task);
        when(taskMapper.mapToTaskResponse(task)).thenReturn(taskResponse);

        ResponseEntity<TaskResponse> response = taskService.createTask(taskRequest);

        verify(taskRepository).insert(task);
        verify(taskProducer).sendTaskNotification(any(NotificationMessage.class));
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(taskResponse);
    }

    @Test
    void updateTask_ShouldUpdateTask_WhenTaskExists() {
        String taskId = "1";
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.mapToTaskResponse(task)).thenReturn(taskResponse);

        ResponseEntity<TaskResponse> response = taskService.updateTask(taskId, taskRequest);

        verify(taskRepository).save(task);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(taskResponse);
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskDoesNotExist() {
        String taskId = "1";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(taskId, taskRequest));

        assertThat(thrown.getMessage()).isEqualTo("No task found.");
    }

    @Test
    void updateStatus_ShouldUpdateTaskStatus_WhenTaskExists() {
        String taskId = "1";
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        ResponseEntity<?> response = taskService.updateStatus(taskId, Status.Completed);
        verify(taskRepository).save(task);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void updateStatus_ShouldThrowException_WhenTaskDoesNotExist() {
        String taskId = "1";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateStatus(taskId, Status.Completed));

        assertThat(thrown.getMessage()).isEqualTo("No task found.");
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        String taskId = "1";
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        ResponseEntity<?> response = taskService.deleteTask(taskId);

        verify(taskRepository).delete(task);
        verify(taskProducer).sendTaskNotification(any(NotificationMessage.class));
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskDoesNotExist() {
        String taskId = "1";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(taskId));

        assertThat(thrown.getMessage()).isEqualTo("No task found.");
    }

    @Test
    void deleteAllTasks_ShouldDeleteAllTasks() {
        String householdId = "household1";

        ResponseEntity<?> response = taskService.deleteAllTasks(householdId);

        verify(taskRepository).deleteByHouseholdId(householdId);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
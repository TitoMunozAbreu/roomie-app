package es.roomie.task.controller;

import es.roomie.task.config.enums.Category;
import es.roomie.task.config.enums.Status;
import es.roomie.task.model.request.TaskResquest;
import es.roomie.task.model.response.TaskResponse;
import es.roomie.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasksByHouseholdIdIn_ValidInput() {
        // Arrange
        List<String> householdIds = Arrays.asList("household1", "household2");
        TaskResponse taskResponse = new TaskResponse(); // Assume this has default values
        when(taskService.getTasksByHouseholdIdIn(householdIds)).thenReturn(ResponseEntity.ok(Collections.singletonList(taskResponse)));

        // Act
        ResponseEntity<List<TaskResponse>> response = taskController.getTasksByHouseholdIdIn(householdIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        verify(taskService, times(1)).getTasksByHouseholdIdIn(householdIds);
    }

    @Test
    void testGetTasksByHouseholdIdIn_EmptyList() {
        // Arrange
        List<String> householdIds = Collections.emptyList();
        when(taskService.getTasksByHouseholdIdIn(householdIds)).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        // Act
        ResponseEntity<List<TaskResponse>> response = taskController.getTasksByHouseholdIdIn(householdIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(taskService, times(1)).getTasksByHouseholdIdIn(householdIds);
    }

    @Test
    void testCreateTask_ValidInput() {
        // Arrange
        TaskResquest taskRequest = new TaskResquest(
                "taskId",
                "householdId",
                "createdBy",
                "title",
                "description",
                Category.Cleaning,
                60,
                "assignedTo",
                LocalDateTime.now(),
                Status.Progress);
        TaskResponse taskResponse = new TaskResponse();
        when(taskService.createTask(taskRequest)).thenReturn(ResponseEntity.ok(taskResponse));

        // Act
        ResponseEntity<TaskResponse> response = taskController.createTask(taskRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(taskService, times(1)).createTask(taskRequest);
    }

    @Test
    void testCreateTask_InvalidInput() {
        // Arrange
        when(taskService.createTask(null)).thenThrow(new IllegalArgumentException("Invalid task request"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskController.createTask(null);
        });
        assertEquals("Invalid task request", exception.getMessage());
        verify(taskService, times(1)).createTask(null);
    }

    @Test
    void testUpdateTask_ValidInput() {
        // Arrange
        String taskId = "1";
        TaskResquest taskRequest = new TaskResquest(
                taskId,
                "householdId",
                "createdBy",
                "title",
                "description",
                Category.Cleaning,
                60,
                "assignedTo",
                LocalDateTime.now(),
                Status.Progress);
        TaskResponse taskResponse = new TaskResponse();
        when(taskService.updateTask(taskId, taskRequest)).thenReturn(ResponseEntity.ok(taskResponse));

        // Act
        ResponseEntity<TaskResponse> response = taskController.updateTask(taskId, taskRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(taskService, times(1)).updateTask(taskId, taskRequest);
    }

    @Test
    void testUpdateTask_TaskNotFound() {
        // Arrange
        String taskId = "1";
        TaskResquest taskRequest = new TaskResquest(
                taskId,
                "householdId",
                "createdBy",
                "title",
                "description",
                Category.Cleaning,
                60,
                "assignedTo",
                LocalDateTime.now(),
                Status.Progress);

        when(taskService.updateTask(taskId, taskRequest)).thenThrow(new RuntimeException("Task not found"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskController.updateTask(taskId, taskRequest);
        });
        assertEquals("Task not found", exception.getMessage());
        verify(taskService, times(1)).updateTask(taskId, taskRequest);
    }

    @Test
    void testUpdateStatus_ValidInput() {
        // Arrange
        String taskId = "1";
        Status status = Status.Completed;
        when(taskService.updateStatus(taskId, status)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = taskController.updateStatus(taskId, status);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).updateStatus(taskId, status);
    }

    @Test
    void testUpdateStatus_TaskNotFound() {
        // Arrange
        String taskId = "1";
        Status status = Status.Completed;
        when(taskService.updateStatus(taskId, status)).thenThrow(new RuntimeException("Task not found"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskController.updateStatus(taskId, status);
        });
        assertEquals("Task not found", exception.getMessage());
        verify(taskService, times(1)).updateStatus(taskId, status);
    }

    @Test
    void testDeleteTask_ValidInput() {
        // Arrange
        String taskId = "1";
        when(taskService.deleteTask(taskId)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = taskController.deleteTask(taskId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    void testDeleteTask_TaskNotFound() {
        // Arrange
        String taskId = "1";
        when(taskService.deleteTask(taskId)).thenThrow(new RuntimeException("Task not found"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskController.deleteTask(taskId);
        });
        assertEquals("Task not found", exception.getMessage());
        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    void testDeleteAllTasks_ValidInput() {
        // Arrange
        String householdId = "household1";
        when(taskService.deleteAllTasks(householdId)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = taskController.deleteAllTasks(householdId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).deleteAllTasks(householdId);
    }

    @Test
    void testDeleteAllTasks_HouseholdNotFound() {
        // Arrange
        String householdId = "household1";
        when(taskService.deleteAllTasks(householdId)).thenThrow(new RuntimeException("Household not found"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskController.deleteAllTasks(householdId);
        });
        assertEquals("Household not found", exception.getMessage());
        verify(taskService, times(1)).deleteAllTasks(householdId);
    }
}
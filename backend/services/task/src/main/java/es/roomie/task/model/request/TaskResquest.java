package es.roomie.task.model.request;

import es.roomie.task.config.enums.Category;
import es.roomie.task.config.enums.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Represents a request for a task.
 * This record holds all relevant information necessary for task management.
 *
 * @param taskId           Unique identifier for the task.
 * @param householdId      Identifier for the household the task belongs to.
 * @param createdBy        Identifier for the user who created the task.
 * @param title            Title of the task.
 * @param description      Detailed description of the task.
 * @param category         Category to which the task belongs.
 * @param estimatedDuration Estimated duration of the task in minutes.
 * @param assignedTo       Identifier for the user the task is assigned to.
 * @param dueDate          Deadline for the task completion.
 * @param status           Current status of the task.
 */
public record TaskResquest(String taskId,
                           @NotBlank String householdId,
                           @NotBlank @Email String createdBy,
                           @NotBlank String title,
                           String description,
                           Category category,
                           @Min(1) int estimatedDuration,
                           @NotBlank @Email String assignedTo,
                           LocalDateTime dueDate,
                           Status status) {}

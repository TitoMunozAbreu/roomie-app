package es.roomie.task.model.request;

import es.roomie.task.config.enums.Category;
import es.roomie.task.config.enums.Status;

import java.time.LocalDateTime;

public record TaskResquest(String householdId,
                           String title,
                           String description,
                           Category category,
                           int estimatedDuration,
                           String assignedTo,
                           LocalDateTime dueDate,
                           Status status) {}

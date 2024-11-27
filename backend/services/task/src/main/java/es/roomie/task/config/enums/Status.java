package es.roomie.task.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The {@code Status} enum represents the possible statuses of a task in the system.
 * It provides a way to categorize tasks based on their current state.
 *
 * <p>Possible statuses include:</p>
 * <ul>
 *     <li>{@link #Pending} - The task has not yet started.</li>
 *     <li>{@link #Progress} - The task is currently being worked on.</li>
 *     <li>{@link #Completed} - The task has been finished successfully.</li>
 *     <li>{@link #Overdue} - The task has not been completed by the expected deadline.</li>
 *     <li>{@link #Cancelled} - The task has been cancelled and will not be completed.</li>
 * </ul>
 */
@Getter
@AllArgsConstructor
public enum Status {
    Pending,
    Progress,
    Completed,
    Overdue,
    Cancelled
}

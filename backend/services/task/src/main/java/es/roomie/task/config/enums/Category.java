package es.roomie.task.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the different categories of tasks.
 */
@Getter
@AllArgsConstructor
public enum Category {
    Cleaning,
    Cooking,
    Laundry,
    Organizing,
    Gardening
}

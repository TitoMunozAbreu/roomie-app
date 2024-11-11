package es.roomie.task.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    Pending,
    Completed,
    Overdue,
    Cancelled
}

package es.roomie.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the availability of a user for a specific day.
 * This class contains the day and the corresponding hours
 * when the user is available.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Availability {
    private String day;
    private List<String> hours;
}

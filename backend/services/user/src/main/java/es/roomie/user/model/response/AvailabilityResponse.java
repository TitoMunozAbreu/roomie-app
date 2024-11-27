package es.roomie.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the availability response model which contains
 * information about a specific day's availability.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailabilityResponse {
    private String day;
    private List<String> hours;
}

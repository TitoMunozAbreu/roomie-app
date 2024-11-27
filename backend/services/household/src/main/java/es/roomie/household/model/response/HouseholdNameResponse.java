package es.roomie.household.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response containing household name details.
 * This class is a data transfer object (DTO) used to convey
 * information about a household name in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseholdNameResponse {
    private String id;
    private String householdName;
}
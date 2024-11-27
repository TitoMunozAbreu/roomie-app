package es.roomie.household.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a household in the system.
 * This class is used to model a household with a unique identifier,
 * a name, and a list of members belonging to that household.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Household {
    @Id private String id;
    private String householdName;
    private List<Member> members;
}

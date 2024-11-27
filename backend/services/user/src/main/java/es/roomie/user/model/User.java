package es.roomie.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
/**
 * Represents a User in the system.
 *
 * This class holds information about a user including their unique identifier,
 * email address, preferences for tasks, history of tasks, and availability.
 *
 * <p>
 * The class includes the following fields:
 * <ul>
 *     <li><code>id</code>: The unique identifier for the user.</li>
 *     <li><code>email</code>: The email address of the user.</li>
 *     <li><code>taskPreferences</code>: A list of preferences for tasks.</li>
 *     <li><code>taskHistories</code>: A list of the user's task history.</li>
 *     <li><code>availabilities</code>: A list of the user's availability times.</li>
 * </ul>
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class User {
    @Id private String id;
    private String email;
    private List<TaskPreference> taskPreferences;
    private List<TaskHistory> taskHistories;
    private List<Availability> availabilities;
}

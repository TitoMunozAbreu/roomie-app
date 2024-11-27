package es.roomie.user.model.response;

import es.roomie.user.model.Availability;
import es.roomie.user.model.TaskHistory;
import es.roomie.user.model.TaskPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The UserResponse class is a data transfer object (DTO) that encapsulates
 * user information to be sent in responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private List<TaskPreference> taskPreferences;
    private List<TaskHistory> taskHistories;
    private List<Availability> availabilities;
}

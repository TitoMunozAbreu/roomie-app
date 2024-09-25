package es.roomie.user.model.response;

import es.roomie.user.model.Availability;
import es.roomie.user.model.TaskHistory;
import es.roomie.user.model.TaskPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

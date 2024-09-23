package es.roomie.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class User {
    @Id private String id;
    private String name;
    private String email;
    private List<TaskPreference> taskPreferences;
    private List<TaskHistory> taskHistories;
    private List<Availability> availabilities;
}

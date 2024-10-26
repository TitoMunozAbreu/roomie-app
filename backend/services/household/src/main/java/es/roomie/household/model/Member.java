package es.roomie.household.model;

import es.roomie.household.config.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Member {
    private String userId;
    private Role role;
    private boolean invitationAccepted;
}

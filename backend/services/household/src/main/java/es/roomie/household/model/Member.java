package es.roomie.household.model;

import es.roomie.household.config.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a member of a household.
 * This class is mapped to a MongoDB document and stores the member's details
 * such as user ID, email, role, and invitation status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Member {
    private String userId;
    private String email;
    private Role role;
    private boolean invitationAccepted;
}

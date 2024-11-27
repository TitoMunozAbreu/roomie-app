package es.roomie.household.model.response;

import es.roomie.household.config.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response model for a household member.
 * This class contains the details of a member within a household.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean invitationAccepted;
}

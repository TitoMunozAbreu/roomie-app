package es.roomie.household.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the different roles within the system.
 * Each role is associated with a string value that represents the role name.
 */
@Getter
@AllArgsConstructor
public enum Role {
    admin("admin"),
    member("member");

    private final String role;
}

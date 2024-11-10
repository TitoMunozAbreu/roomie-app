package es.roomie.household.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    admin("admin"),
    member("member");

    private final String role;
}

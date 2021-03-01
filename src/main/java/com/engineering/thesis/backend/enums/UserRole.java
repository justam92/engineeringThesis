package com.engineering.thesis.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    DOCTOR("ROLE_DOCTOR"),
    PATIENT("ROLE_PATIENT"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    public static UserRole parse(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equals(role)) {
                return userRole;
            }
        }
        throw new RuntimeException("Role :" + role + "doesn't exist.");
    }
}

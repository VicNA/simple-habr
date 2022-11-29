package ru.geekbrains.habr.services.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum UserRole {
    ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN;

    private Set<UserRole> roles;

    static {
        ROLE_USER.roles = Set.of(ROLE_USER);
        ROLE_MODERATOR.roles = Set.of(ROLE_USER, ROLE_MODERATOR);
        ROLE_ADMIN.roles = Set.of(ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN);
    }
}

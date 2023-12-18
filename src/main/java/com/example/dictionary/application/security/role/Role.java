package com.example.dictionary.application.security.role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.dictionary.application.security.role.Permission.USERS_READ;
import static com.example.dictionary.application.security.role.Permission.USER_WRITE;
import static com.example.dictionary.application.security.role.Permission.WORD_READ;
import static com.example.dictionary.application.security.role.Permission.WORD_WRITE;

public enum Role {
    ADMIN(Set.of(WORD_READ, WORD_WRITE, USER_WRITE, USERS_READ)),
    EDITOR(Set.of(WORD_READ, WORD_WRITE, USERS_READ)),
    TEACHER(Set.of(WORD_READ)),
    LEARNER(Set.of(WORD_READ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}

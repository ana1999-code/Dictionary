package com.example.dictionary.application.security.role;

public enum Permission {

    WORD_READ("word:read"),
    WORD_WRITE("word:write"),
    USERS_READ("users:read"),
    USER_WRITE("users:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

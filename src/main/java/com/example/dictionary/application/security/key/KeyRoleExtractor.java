package com.example.dictionary.application.security.key;

import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.security.role.Role;

import java.util.HashMap;
import java.util.Map;

public class KeyRoleExtractor {

    private final static Map<String, Role> KEY_ROLE_MAP = new HashMap<>();

    static  {
        KEY_ROLE_MAP.put("admin", Role.ADMIN);
        KEY_ROLE_MAP.put("editor", Role.EDITOR);
        KEY_ROLE_MAP.put("teacher", Role.TEACHER);
        KEY_ROLE_MAP.put("learner", Role.LEARNER);
    }

    public static Role getRole(String key){
        Role role = KEY_ROLE_MAP.get(key);
        if (role == null){
            throw new ResourceNotFoundException("Invalid key");
        }

        return role;
    }
}

package com.example.dictionary.application.validator.password;

import com.example.dictionary.application.exception.InvalidPasswordException;

import java.util.Map;

public class PasswordValidator {

    public static void validate(String password) {
        PasswordErrorMapGenerator passwordErrorMapGenerator = new PasswordErrorMapGenerator();
        final Map<String, String> errorMap = passwordErrorMapGenerator.getErrorMap(password);

        if (!errorMap.isEmpty()) {
            throw new InvalidPasswordException(errorMap);
        }
    }
}

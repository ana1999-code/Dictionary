package com.example.dictionary.application.validator;

import com.example.dictionary.application.exception.InvalidPasswordException;

import java.util.HashMap;
import java.util.Map;

public class PasswordValidator {

    public static void validate(String password) {

        final Map<String, String> errorMap = getErrorMap(password);

        if (!errorMap.isEmpty()) {
            throw new InvalidPasswordException(errorMap);
        }
    }

    private static Map<String, String> getErrorMap(String password) {
        final Map<String, String> errorMap = new HashMap<>();

        if (password.length() < 8) {
            errorMap.put("length", "Password should have at least 8 chars");
        }

        if (!containsUppercase(password)) {
            errorMap.put("upper_letter", "Password should have at least one uppercase char");
        }

        if (!containsLowercase(password)) {
            errorMap.put("lower_letter", "Password should have at least one lowercase char");
        }

        if (!containsNumber(password)) {
            errorMap.put("number", "Password should have at least one number");
        }

        if (containsWhitespace(password)) {
            errorMap.put("whitespace", "Password should not have any whitespaces");
        }

        if (!containsSpecialCharacter(password)) {
            errorMap.put("special_char", "Password should have at least one special char");
        }
        return errorMap;
    }

    private static boolean containsUppercase(String word) {
        for (char c : word.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsLowercase(String word) {
        for (char c : word.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsNumber(String word) {
        for (char c : word.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsWhitespace(String word) {
        for (char c : word.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsSpecialCharacter(String word) {
        for (char c : word.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                return true;
            }
        }
        return false;
    }
}

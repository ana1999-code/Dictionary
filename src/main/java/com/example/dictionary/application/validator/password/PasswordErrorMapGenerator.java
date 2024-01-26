package com.example.dictionary.application.validator.password;

import com.example.dictionary.application.i18n.LocaleConfig;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PasswordErrorMapGenerator {

    private final MessageSource messageSource;

    public PasswordErrorMapGenerator() {
        LocaleConfig localeConfig = new LocaleConfig();
        this.messageSource = localeConfig.messageSource();
    }

    public Map<String, String> getErrorMap(String password) {
        final Map<String, String> errorMap = new HashMap<>();

        if (password.length() < 8) {
            errorMap.put("length",
                    messageSource.getMessage("password.length", null, Locale.getDefault()));
        }

        if (!containsUppercase(password)) {
            errorMap.put("upper_letter",
                    messageSource.getMessage("password.uppercase",null, Locale.getDefault()));
        }

        if (!containsLowercase(password)) {
            errorMap.put("lower_letter",
                    messageSource.getMessage("password.lowercase",null, Locale.getDefault()));
        }

        if (!containsNumber(password)) {
            errorMap.put("number",
                    messageSource.getMessage("password.number",null, Locale.getDefault()));
        }

        if (containsWhitespace(password)) {
            errorMap.put("whitespace",
                    messageSource.getMessage("password.whitespace",null, Locale.getDefault()));
        }

        if (!containsSpecialCharacter(password)) {
            errorMap.put("special_char",
                    messageSource.getMessage("password.specialchar",null, Locale.getDefault()));
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

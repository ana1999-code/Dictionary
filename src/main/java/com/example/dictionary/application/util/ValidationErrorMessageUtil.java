package com.example.dictionary.application.util;

import org.springframework.batch.item.validator.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationErrorMessageUtil {

    private static final Pattern ERROR_PATTERN = Pattern.compile(
            ".*?message \\[([^']+)].*?message \\[([^']+)]");

    public static String extractValidationErrorMessage(ValidationException validationException) {
        String errorMessage = validationException.getMessage();
        Matcher matcher = ERROR_PATTERN.matcher(errorMessage);

        if (matcher.find()) {
            return matcher.group(2);
        }

        return errorMessage.trim();
    }
}

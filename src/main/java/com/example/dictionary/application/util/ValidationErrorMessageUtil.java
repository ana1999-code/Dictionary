package com.example.dictionary.application.util;

import org.springframework.batch.item.validator.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationErrorMessageUtil {

    private static final Pattern ERROR_PATTERN = Pattern.compile(
            "Field error in object '([^']+)' on field '([^']+)':.*?message \\[([^']+)].*?message ([^']+)");

    public static String extractValidationErrorMessage(ValidationException validationException) {
        String errorMessage = validationException.getMessage();
        Matcher matcher = ERROR_PATTERN.matcher(errorMessage);

        if (matcher.find()) {
            String objectName = matcher.group(1);
            String fieldName = matcher.group(2);
            String message = matcher.group(4);

            return String.format("Field error in object '%s' on field '%s': message %s", objectName, fieldName, message);
        }

        return errorMessage.trim();
    }
}

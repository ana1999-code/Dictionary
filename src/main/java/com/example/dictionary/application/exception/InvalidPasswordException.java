package com.example.dictionary.application.exception;

import java.util.Map;

public class InvalidPasswordException extends RuntimeException {

    private final Map<String, String> errorMap;

    public InvalidPasswordException(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }
}

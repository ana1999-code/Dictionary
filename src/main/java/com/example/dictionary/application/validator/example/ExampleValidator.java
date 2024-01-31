package com.example.dictionary.application.validator.example;

import com.example.dictionary.application.exception.IllegalOperationException;

import java.util.Map;

public class ExampleValidator {

    public static void validate(String name, String example) {
        ExampleValidatorErrorGenerator generator = new ExampleValidatorErrorGenerator();
        Map<String, String> errorMap = generator.validate(name, example);

        if (!errorMap.isEmpty()) {
            throw new IllegalOperationException(errorMap.get("error"));
        }
    }
}

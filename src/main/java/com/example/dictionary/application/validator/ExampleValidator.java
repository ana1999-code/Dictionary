package com.example.dictionary.application.validator;

import com.example.dictionary.application.exception.IllegalOperationException;

public class ExampleValidator {

    public static void validate(String name, String example) {
        if (!example.toLowerCase().contains(name.toLowerCase())) {
            throw new IllegalOperationException(
                    "Provided example does not contain the word %s".formatted(name));
        }
    }
}

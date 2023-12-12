package com.example.dictionary.application.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String resource) {
        super(resource);
    }
}

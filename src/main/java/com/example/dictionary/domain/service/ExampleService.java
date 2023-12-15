package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.Example;

import java.util.List;
import java.util.Optional;

public interface ExampleService {

    Optional<Example> getExampleByText(String text);

    List<Example> getAllExamples();
}

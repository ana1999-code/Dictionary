package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.repository.ExampleRepository;
import com.example.dictionary.domain.service.ExampleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    public ExampleServiceImpl(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Override
    public Optional<Example> getExampleByText(String text) {
        return exampleRepository.findExampleByText(text);
    }

    @Override
    public List<Example> getAllExamples() {
        return exampleRepository.findAll();
    }
}

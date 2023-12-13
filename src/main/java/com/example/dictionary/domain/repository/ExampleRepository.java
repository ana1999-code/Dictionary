package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExampleRepository extends JpaRepository<Example, Integer> {

    Optional<Example> findExampleByText(String text);
}

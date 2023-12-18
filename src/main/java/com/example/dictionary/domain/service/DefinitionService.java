package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.Definition;

import java.util.List;
import java.util.Optional;

public interface DefinitionService {

    Optional<Definition> getDefinitionByText(String text);

    List<Definition> getAllDefinitions();

    Definition saveDefinition(Definition definition);
}

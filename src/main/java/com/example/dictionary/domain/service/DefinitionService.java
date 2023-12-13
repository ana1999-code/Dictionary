package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.Definition;

import java.util.Optional;

public interface DefinitionService {

    Optional<Definition> getDefinitionByText(String text);
}

package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.repository.DefinitionRepository;
import com.example.dictionary.domain.service.DefinitionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefinitionServiceImpl implements DefinitionService {

    private final DefinitionRepository definitionRepository;

    public DefinitionServiceImpl(DefinitionRepository definitionRepository) {
        this.definitionRepository = definitionRepository;
    }

    @Override
    public Optional<Definition> getDefinitionByText(String text) {
        return definitionRepository.getDefinitionByText(text);
    }
}

package com.example.dictionary.application.validator;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.IllegalOperationException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.domain.service.WordService;
import org.springframework.stereotype.Component;

@Component
public class WordValidator {

    private final WordService wordService;

    public WordValidator(WordService wordService) {
        this.wordService = wordService;
    }

    public void validate(WordDto wordDto) {
        String name = wordDto.getName();

        if (wordService.getWordByName(name).isPresent()) {
            throw new DuplicateResourceException("Word %s already exists".formatted(name));
        }

        if (wordDto.getDefinitions().isEmpty()) {
            throw new ResourceNotFoundException("Word %s should have at least one definition".formatted(name));
        }

        if (!wordDto.getExamples().isEmpty()) {
            wordDto.getExamples()
                    .stream()
                    .filter(example -> !example.getText().contains(name))
                    .findAny()
                    .ifPresent(example -> {
                        throw new IllegalOperationException(
                                "Provided example does not contain the word %s".formatted(name));
                    });
        }
    }
}

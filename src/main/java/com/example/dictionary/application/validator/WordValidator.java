package com.example.dictionary.application.validator;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.validator.example.ExampleValidator;
import com.example.dictionary.domain.service.WordService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class WordValidator {

    private final MessageSource messageSource;

    private final WordService wordService;

    public WordValidator(MessageSource messageSource, WordService wordService) {
        this.messageSource = messageSource;
        this.wordService = wordService;
    }

    public void validate(WordDto wordDto) {
        String name = wordDto.getName();

        if (wordService.getWordByName(name).isPresent()) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("word.error.exists.message",
                            new Object[]{name},
                            Locale.getDefault()));
        }

        if (wordDto.getDefinitions().isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("word.definition.error.message",
                            new Object[]{name},
                            Locale.getDefault()));
        }

        if (!wordDto.getExamples().isEmpty()) {
            wordDto.getExamples()
                    .forEach(example -> ExampleValidator.validate(name, example.getText()));
        }
    }
}

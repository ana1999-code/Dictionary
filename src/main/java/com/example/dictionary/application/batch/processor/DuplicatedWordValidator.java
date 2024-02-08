package com.example.dictionary.application.batch.processor;

import com.example.dictionary.application.exception.DuplicateResourceException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DuplicatedWordValidator {

    private final MessageSource messageSource;

    private final List<String> wordNames = new ArrayList<>();

    public DuplicatedWordValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void addProcessedWord(String word){
        wordNames.add(word);
    }

    public void validateWordPresence(String word){
        if (wordNames.contains(word)) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("word.file.duplicated", null, Locale.getDefault())
            );
        }
    }

    public void resetWordNames() {
        wordNames.clear();
    }
}

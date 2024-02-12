package com.example.dictionary.application.validator;

import com.example.dictionary.application.dto.DictionaryDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.domain.service.DictionaryService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DictionaryValidator {

    private final MessageSource messageSource;

    private final DictionaryService dictionaryService;

    public DictionaryValidator(MessageSource messageSource, DictionaryService dictionaryService) {
        this.messageSource = messageSource;
        this.dictionaryService = dictionaryService;
    }

    public void validate(DictionaryDto dictionaryDto) {
        String name = dictionaryDto.getName();
        String url = dictionaryDto.getUrl();

        if (dictionaryService.existsDictionaryByName(name)) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("dictionary.name.error.exists.message", new Object[]{name}, Locale.getDefault())
            );
        }

        if (!url.isEmpty() && dictionaryService.existsDictionaryByUrl(url)) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("dictionary.url.error.exists.message", new Object[]{url}, Locale.getDefault())
            );
        }
    }
}

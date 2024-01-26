package com.example.dictionary.application.validator.example;

import com.example.dictionary.application.i18n.LocaleConfig;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExampleValidatorErrorGenerator {

    private final MessageSource messageSource;

    public ExampleValidatorErrorGenerator() {
        LocaleConfig localeConfig = new LocaleConfig();
        this.messageSource = localeConfig.messageSource();
    }

    public Map<String, String> validate(String name, String example) {
        Map<String, String> errorMap = new HashMap<>();
        if (!example.toLowerCase().contains(name.toLowerCase())) {
            errorMap.put("error",
                    messageSource.getMessage("word.example.error.message", new Object[]{name}, Locale.getDefault()));
        }

        return errorMap;
    }
}

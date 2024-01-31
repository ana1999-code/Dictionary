package com.example.dictionary.ui.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class SimpleI18NProvider implements I18NProvider {

    public static final Locale ITALIAN = new Locale("it");

    public static final Locale ENGLISH = new Locale("en");

    private Map<String, ResourceBundle> localeMap;

    private static SimpleI18NProvider simpleI18NProvider;

    private SimpleI18NProvider() {
    }

    @PostConstruct
    private void initMap() {
        localeMap = new HashMap<>();
        for (final Locale locale : getProvidedLocales()) {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages", locale);
            localeMap.put(locale.getLanguage(), resourceBundle);
        }
    }

    @Override
    public List<Locale> getProvidedLocales() {
        return List.of(ENGLISH, ITALIAN);
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        return MessageFormat
                .format(localeMap.get(locale.getLanguage()).getString(key), params);
    }

    public static SimpleI18NProvider getSimpleI18NProvider() {
        if (simpleI18NProvider == null) {
            simpleI18NProvider = new SimpleI18NProvider();
        }

        return simpleI18NProvider;
    }
}

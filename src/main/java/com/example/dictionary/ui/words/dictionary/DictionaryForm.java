package com.example.dictionary.ui.words.dictionary;

import com.example.dictionary.application.dto.DictionaryDto;
import com.example.dictionary.application.facade.DictionaryFacade;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class DictionaryForm extends FormLayout {

    private TextField name;

    private TextField url;

    private BeanValidationBinder<DictionaryDto> dictionaryBinder;

    private final DictionaryFacade dictionaryFacade;

    private DictionaryDto dictionaryDto;

    public DictionaryForm(DictionaryFacade dictionaryFacade) {
        this.dictionaryFacade = dictionaryFacade;

        setupForm();
    }

    private void setupForm() {
        name = new TextField(getTranslation("word.source.name"));
        name.setRequired(true);
        url = new TextField(getTranslation("word.source.url"));

        add(name, url);

        dictionaryDto = new DictionaryDto();
        dictionaryBinder = new BeanValidationBinder<>(DictionaryDto.class);
        dictionaryBinder
                .forField(name)
                .asRequired(getTranslation("dictionary.name.not.null.error.message"))
                .bind("name");

        dictionaryBinder.bind(url, "url");
    }

    public void saveDictionary() throws ValidationException {
        dictionaryBinder.writeBean(dictionaryDto);
        dictionaryFacade.addDictionary(dictionaryDto);
    }

    public String getName() {
        return name.getValue();
    }

    public void reset() {
        name.clear();
        url.clear();
        remove(name, url);
        setupForm();
    }
}

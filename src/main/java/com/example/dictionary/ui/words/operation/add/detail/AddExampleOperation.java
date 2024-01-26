package com.example.dictionary.ui.words.operation.add.detail;

import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.add.AddOperationTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class AddExampleOperation extends AddOperationTemplate {

    private BeanValidationBinder<ExampleDto> exampleBinder =
            new BeanValidationBinder<>(ExampleDto.class);

    public AddExampleOperation(WordFacade wordFacade, String wordName, WordView wordView) {
        super(wordFacade, wordName, wordView);
    }

    @Override
    protected String getDescription() {
        return getTranslation("add") + " " + getTranslation("word.example");
    }

    @Override
    protected void createBinderAndSave(TextArea detail) throws ValidationException {
        ExampleDto exampleDto = new ExampleDto(detail.getValue());
        exampleBinder.bind(detail, "text");
        exampleBinder.writeBean(exampleDto);
        wordFacade
                .addExampleToWord(wordName, exampleDto);
    }
}

package com.example.dictionary.ui.words.operation;

import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.facade.WordFacade;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class AddExampleOperation extends WordOperationTemplate {

    private BeanValidationBinder<ExampleDto> exampleBinder =
            new BeanValidationBinder<>(ExampleDto.class);

    public AddExampleOperation(WordFacade wordFacade, String wordName) {
        super(wordFacade, wordName);
    }

    @Override
    protected String getDescription() {
        return "Add Example";
    }

    @Override
    protected void createBinderAndSave(TextField detail) throws ValidationException {
        ExampleDto exampleDto = new ExampleDto(detail.getValue());
        exampleBinder.bind(detail, "text");
        exampleBinder.writeBean(exampleDto);
        wordFacade
                .addExampleToWord(wordName, exampleDto);
    }
}

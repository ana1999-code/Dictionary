package com.example.dictionary.ui.words.operation;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.facade.WordFacade;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class AddDefinitionOperation extends WordOperationTemplate {

    private BeanValidationBinder<DefinitionDto> definitionBinder =
            new BeanValidationBinder<>(DefinitionDto.class);

    public AddDefinitionOperation(WordFacade wordFacade, String wordName) {
        super(wordFacade, wordName);
    }

    @Override
    protected String getDescription() {
        return "Add Definition";
    }

    @Override
    protected void createBinderAndSave(TextField detail) throws ValidationException {
        DefinitionDto definitionDto = new DefinitionDto(detail.getValue());
        definitionBinder.bind(detail, "text");
        definitionBinder.writeBean(definitionDto);
        wordFacade
                .addDefinitionToWord(wordName, definitionDto);
    }
}

package com.example.dictionary.ui.words.operation.add.detail;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.add.AddOperationTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class AddDefinitionOperation extends AddOperationTemplate {

    private BeanValidationBinder<DefinitionDto> definitionBinder =
            new BeanValidationBinder<>(DefinitionDto.class);

    public AddDefinitionOperation(WordFacade wordFacade, String wordName, WordView wordView) {
        super(wordFacade, wordName, wordView);
    }

    @Override
    protected String getDescription() {
        return getTranslation("add") + " " + getTranslation("word.definition");
    }

    @Override
    protected void createBinderAndSave(TextArea detail) throws ValidationException {
        DefinitionDto definitionDto = new DefinitionDto(detail.getValue());
        definitionBinder.bind(detail, "text");
        definitionBinder.writeBean(definitionDto);
        wordFacade
                .addDefinitionToWord(wordName, definitionDto);
    }
}

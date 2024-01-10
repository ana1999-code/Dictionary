package com.example.dictionary.ui.words.operation.remove;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.data.binder.Binder;

public class RemoveDefinitionOperation extends RemoveOperationTemplate {

    private final DefinitionDto definition;

    private Binder<DefinitionDto> definitionBinder;

    public RemoveDefinitionOperation(WordFacade wordFacade, String wordName, DefinitionDto definition, WordView wordView) {
        super(wordFacade, wordName, wordView);
        this.definition = definition;
    }

    @Override
    protected String getDescription() {
        return "Delete definition";
    }

    @Override
    protected void createBinder() {
        definitionBinder = new Binder<>(DefinitionDto.class);
    }

    @Override
    protected void bind() {
        definitionBinder.setBean(definition);
        definitionBinder.bind(wordTextFieldForm.getDetail(), "text");
    }

    @Override
    public H4 getConfirmationMessage() {
        return new H4("Are you sure you want to delete definition [%s]?".formatted(definition.getText()));
    }

    @Override
    public void executeRemoveOperation() {
        wordFacade.removeDefinitionFromWord(wordName, definition);
    }
}

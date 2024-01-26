package com.example.dictionary.ui.words.operation.remove.detail;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.remove.RemoveOperationTemplate;
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
        return getTranslation("delete") + " " + getTranslation("word.definition");
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
        return new H4(getTranslation("word.delete.message", getTranslation("word.definition"), definition.getText()));
    }

    @Override
    public void executeRemoveOperation() {
        wordFacade.removeDefinitionFromWord(wordName, definition);
    }
}

package com.example.dictionary.ui.words.operation.remove.detail;

import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.remove.RemoveOperationTemplate;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.data.binder.Binder;

public class RemoveExampleOperation extends RemoveOperationTemplate {

    private final ExampleDto example;

    private Binder<ExampleDto> exampleBinder;

    public RemoveExampleOperation(WordFacade wordFacade, String wordName, ExampleDto example, WordView wordView) {
        super(wordFacade, wordName, wordView);
        this.example = example;
    }

    @Override
    protected String getDescription() {
        return getTranslation("delete") + " " + getTranslation("word.example");
    }

    @Override
    protected void createBinder() {
        exampleBinder = new Binder<>(ExampleDto.class);
    }

    @Override
    protected void bind() {
        exampleBinder.setBean(example);
        exampleBinder.bind(wordTextFieldForm.getDetail(), "text");
    }

    @Override
    protected H4 getConfirmationMessage() {
        return new H4(getTranslation("word.delete.message", getTranslation("word.antonym"), example.getText()));
    }

    @Override
    protected void executeRemoveOperation() {
        wordFacade.removeExampleFromWord(wordName, example);
    }
}

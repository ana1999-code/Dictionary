package com.example.dictionary.ui.words.operation.remove;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.data.binder.Binder;

public abstract class RemoveWordOperation extends RemoveOperationTemplate {

    protected final WordDto word;

    private Binder<WordDto> wordDtoBinder;

    protected RemoveWordOperation(WordFacade wordFacade, String wordName, WordDto word, WordView wordView) {
        super(wordFacade, wordName, wordView);
        this.word = word;
    }

    @Override
    protected abstract String getDescription();

    @Override
    protected void createBinder() {
        wordDtoBinder = new Binder<>(WordDto.class);
    }

    @Override
    protected void bind() {
        wordDtoBinder.setBean(word);
        wordDtoBinder.bind(wordTextFieldForm.getDetail(), "name");
    }

    @Override
    protected abstract H4 getConfirmationMessage();

    @Override
    protected abstract void executeRemoveOperation();
}

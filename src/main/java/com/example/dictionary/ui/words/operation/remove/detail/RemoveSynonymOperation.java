package com.example.dictionary.ui.words.operation.remove.detail;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.vaadin.flow.component.html.H4;

public class RemoveSynonymOperation extends RemoveWordOperation {

    public RemoveSynonymOperation(WordFacade wordFacade, String wordName, WordDto word, WordView wordView) {
        super(wordFacade, wordName, word, wordView);
    }

    @Override
    protected String getDescription() {
        return getTranslation("delete") + " " + getTranslation("word.synonym");
    }

    @Override
    protected H4 getConfirmationMessage() {
        return new H4(getTranslation("word.delete.message", getTranslation("word.synonym"), word.getName()));
    }

    @Override
    protected void executeRemoveOperation() {
        wordFacade.removeSynonym(wordName, word);
    }
}

package com.example.dictionary.ui.words.operation;

import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordDialog;
import com.example.dictionary.ui.words.WordTextFieldForm;
import com.vaadin.flow.component.html.Div;

public abstract class OperationTemplate extends Div {

    protected final WordFacade wordFacade;

    protected final String wordName;

    protected WordTextFieldForm wordTextFieldForm;

    protected WordDialog wordDialog;

    protected OperationTemplate(WordFacade wordFacade, String wordName) {
        this.wordFacade = wordFacade;
        this.wordName = wordName;
    }

    public void execute() {
        createWordDetailForm();
        createWordDialog();
    }

    protected void openWordDialog() {
        wordDialog.getDialog().open();
    }

    private void createWordDetailForm() {
        wordTextFieldForm = new WordTextFieldForm();
    }

    protected abstract void createWordDialog();

    protected void closeDialog() {
        wordDialog.getDialog().close();
    }

    protected abstract String getDescription();

    public WordTextFieldForm getWordTextFieldForm() {
        return wordTextFieldForm;
    }
}

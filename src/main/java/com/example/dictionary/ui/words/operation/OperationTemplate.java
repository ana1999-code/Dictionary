package com.example.dictionary.ui.words.operation;

import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.common.CommonDialog;
import com.example.dictionary.ui.words.WordTextFieldForm;
import com.vaadin.flow.component.html.Div;

public abstract class OperationTemplate extends Div {

    protected final WordFacade wordFacade;

    protected final String wordName;

    protected WordTextFieldForm wordTextFieldForm = new WordTextFieldForm();

    protected CommonDialog commonDialog;

    protected OperationTemplate(WordFacade wordFacade, String wordName) {
        this.wordFacade = wordFacade;
        this.wordName = wordName;
    }

    public void execute() {
        createWordDialog();
    }

    protected void openWordDialog() {
        commonDialog.getDialog().open();
    }

    protected abstract void createWordDialog();

    protected void closeDialog() {
        commonDialog.getDialog().close();
    }

    protected abstract String getDescription();

    public WordTextFieldForm getWordTextFieldForm() {
        return wordTextFieldForm;
    }
}

package com.example.dictionary.ui.words.operation;

import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordTextFieldForm;
import com.example.dictionary.ui.words.WordDialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;

import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;

public abstract class WordOperationTemplate {

    protected final WordFacade wordFacade;

    protected final String wordName;

    private WordTextFieldForm wordTextFieldForm;

    private WordDialog wordDialog;

    protected WordOperationTemplate(WordFacade wordFacade, String wordName) {
        this.wordFacade = wordFacade;
        this.wordName = wordName;
    }

    public void execute(){
        createWordDetailForm();
        createWordDialog();
        openWordDialog();
        configureSaveButton();
        configureCancelButton();
        configureResetButton();
    }

    private void createWordDetailForm() {
        wordTextFieldForm = new WordTextFieldForm();
    }

    private void createWordDialog() {
        wordDialog = new WordDialog(wordTextFieldForm, getDescription());
    }

    protected abstract String getDescription();

    private void openWordDialog() {
        wordDialog.getDialog().open();
    }

    private void configureSaveButton() {
        wordDialog.getSaveButton().addClickListener(clickEvent -> {
            TextField detail = wordTextFieldForm.getDetail();
            createDtoAndExecute(detail);
        });
    }

    private void configureCancelButton() {
        wordDialog.getCancelButton()
                .addClickListener(clickEvent -> closeDialog());
    }

    private void closeDialog() {
        wordDialog.getDialog().close();
    }

    private void configureResetButton() {
        wordDialog.getResetButton()
                .addClickListener(clickEvent -> wordTextFieldForm.getDetail().clear());
    }

    private void createDtoAndExecute(TextField detail) {
        try {
            createBinderAndSave(detail);
            closeDialog();
            showSuccess("[%s] Successfully Added".formatted(detail.getValue()));
        } catch (RuntimeException e) {
            showNotification(e.getMessage());
            e.printStackTrace();
        } catch (ValidationException ignored) {
        }
    }

    protected abstract void createBinderAndSave(TextField detail) throws ValidationException;
}

package com.example.dictionary.ui.words;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;

public class WordDialog extends Div {

    private Dialog dialog;

    private WordForm wordForm;

    private Button saveButton = new Button("Save");

    private Button cancelButton = new Button("Cancel");

    private Button resetButton = new Button("Reset");

    public WordDialog(WordForm wordForm) {
        this.wordForm = wordForm;
        dialog = new Dialog();

        dialog.setHeaderTitle("Add New Word");
        dialog.setWidth("50%");

        dialog.add(wordForm);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        dialog.getFooter().add(cancelButton, resetButton, saveButton);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getResetButton() {
        return resetButton;
    }
}

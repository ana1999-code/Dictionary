package com.example.dictionary.ui.words;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;

public class WordDialog extends Div {

    private Dialog dialog;

    private Component wordForm;

    private Button saveButton = new Button("Save");

    private Button cancelButton = new Button("Cancel");

    private Button resetButton = new Button("Reset");

    public WordDialog(Component wordForm, String description) {
        this.wordForm = wordForm;
        dialog = new Dialog();

        dialog.setHeaderTitle(description);
        dialog.setWidth("30%");

        dialog.add(wordForm);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.getStyle().set("margin-right", "auto");

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

package com.example.dictionary.ui.words;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;

public class WordDialog extends Div {

    private Dialog dialog;

    private Component wordForm;

    private Button secondRightButton = new Button();

    private Button leftButton = new Button();

    private Button firstRightButton = new Button();

    public WordDialog(Component wordForm, String description) {
        this.wordForm = wordForm;
        dialog = new Dialog();

        dialog.setHeaderTitle(description);
        dialog.setWidth("30%");
        dialog.add(wordForm);

        leftButton.getStyle().set("margin-right", "auto");

        dialog.getFooter().add(leftButton, firstRightButton, secondRightButton);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Button getSecondRightButton() {
        return secondRightButton;
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public Button getFirstRightButton() {
        return firstRightButton;
    }
}

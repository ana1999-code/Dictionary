package com.example.dictionary.ui.words;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import static com.example.dictionary.ui.util.UiUtils.getCloseButton;

public class WordTextFieldForm extends FormLayout {

    private TextField detail = new TextField();

    private Button delete = getCloseButton();

    public WordTextFieldForm() {
        detail.setRequired(true);
        detail.setWidthFull();
        delete.setVisible(false);
        HorizontalLayout layout = new HorizontalLayout(detail, delete);
        add(layout);
    }

    public TextField getDetail() {
        return detail;
    }

    public Button getDelete() {
        return delete;
    }
}

package com.example.dictionary.ui.words;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import static com.example.dictionary.ui.util.UiUtils.getCloseButton;

public class WordTextFieldForm extends FormLayout {

    private TextArea detail = new TextArea();

    private Button delete = getCloseButton();

    private HorizontalLayout formLayout = new HorizontalLayout();

    public WordTextFieldForm() {
        detail.setRequired(true);
        detail.setWidthFull();
        delete.setVisible(false);
        formLayout.add(detail, delete);
        formLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, detail, delete);
        add(formLayout);
    }

    public TextArea getDetail() {
        return detail;
    }

    public Button getDelete() {
        return delete;
    }

    public HorizontalLayout getFormLayout() {
        return formLayout;
    }
}

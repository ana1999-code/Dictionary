package com.example.dictionary.ui.words.operation.add;

import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordDialog;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.OperationTemplate;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.ValidationException;

import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
import static com.example.dictionary.ui.words.operation.DataRefresher.refresh;
import static com.vaadin.flow.component.button.ButtonVariant.*;

public abstract class AddOperationTemplate extends OperationTemplate {

    private final WordView wordView;

    protected AddOperationTemplate(WordFacade wordFacade, String wordName, WordView wordView) {
        super(wordFacade, wordName);
        this.wordView = wordView;
    }

    @Override
    public void execute() {
        super.execute();
        openWordDialog();
        configureSaveButton();
        configureCancelButton();
        configureResetButton();
    }

    private void configureSaveButton() {
        wordDialog.getSecondRightButton().setText("Save");
        wordDialog.getSecondRightButton().addThemeVariants(LUMO_PRIMARY);
        wordDialog.getSecondRightButton().addClickListener(clickEvent -> {
            TextArea detail = wordTextFieldForm.getDetail();
            createDtoAndExecute(detail);
        });
    }

    private void configureCancelButton() {
        wordDialog.getLeftButton().setText("Cancel");
        wordDialog.getLeftButton().addThemeVariants(LUMO_ERROR);
        wordDialog.getLeftButton()
                .addClickListener(clickEvent -> closeDialog());
    }

    private void configureResetButton() {
        wordDialog.getFirstRightButton().setText("Reset");
        wordDialog.getFirstRightButton().addThemeVariants(LUMO_TERTIARY);
        wordDialog.getFirstRightButton()
                .addClickListener(clickEvent -> wordTextFieldForm.getDetail().clear());
    }

    private void createDtoAndExecute(TextArea detail) {
        try {
            createBinderAndSave(detail);
            closeDialog();
            refresh(wordView, wordFacade);
            showSuccess("[%s] Successfully Added".formatted(detail.getValue()));
        } catch (RuntimeException e) {
            showNotification(e.getMessage());
        } catch (ValidationException ignored) {
        }
    }

    protected void createWordDialog() {
        wordDialog = new WordDialog(wordTextFieldForm, getDescription());
    }

    protected abstract void createBinderAndSave(TextArea detail) throws ValidationException;
}

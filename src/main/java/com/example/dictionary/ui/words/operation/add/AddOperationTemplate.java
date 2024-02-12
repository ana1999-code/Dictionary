package com.example.dictionary.ui.words.operation.add;

import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.common.CommonDialog;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.OperationTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.ValidationException;

import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
import static com.example.dictionary.ui.words.operation.DataRefresher.refresh;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY;

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
        commonDialog.getSecondRightButton().setText(getTranslation("save"));
        commonDialog.getSecondRightButton().addThemeVariants(LUMO_PRIMARY);
        commonDialog.getSecondRightButton().addClickListener(clickEvent -> {
            TextArea detail = wordTextFieldForm.getDetail();
            createDtoAndExecute(detail);
        });
    }

    private void configureCancelButton() {
        commonDialog.getLeftButton().setText(getTranslation("cancel"));
        commonDialog.getLeftButton().addThemeVariants(LUMO_ERROR);
        commonDialog.getLeftButton()
                .addClickListener(clickEvent -> closeDialog());
    }

    private void configureResetButton() {
        commonDialog.getFirstRightButton().setText(getTranslation("reset"));
        commonDialog.getFirstRightButton().addThemeVariants(LUMO_TERTIARY);
        commonDialog.getFirstRightButton()
                .addClickListener(clickEvent -> wordTextFieldForm.getDetail().clear());
    }

    private void createDtoAndExecute(TextArea detail) {
        try {
            createBinderAndSave(detail);
            closeDialog();
            refresh(wordView, wordFacade);
            showSuccess(getTranslation("add.success.message", detail.getValue()));
        } catch (RuntimeException e) {
            showNotification(e.getMessage());
        } catch (ValidationException ignored) {
        }
    }

    protected void createWordDialog() {
        commonDialog = new CommonDialog(wordTextFieldForm, getDescription());
    }

    protected abstract void createBinderAndSave(TextArea detail) throws ValidationException;
}

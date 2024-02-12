package com.example.dictionary.ui.words.operation.remove;

import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.common.CommonDialog;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.OperationTemplate;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;

import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
import static com.example.dictionary.ui.words.operation.DataRefresher.refresh;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;

public abstract class RemoveOperationTemplate extends OperationTemplate {

    private final WordView wordView;

    protected RemoveOperationTemplate(WordFacade wordFacade, String wordName, WordView wordView) {
        super(wordFacade, wordName);
        this.wordView = wordView;
    }

    @Override
    public void execute() {
        super.execute();
        createBinder();
        setupDeleteFieldButton();
        bind();
    }

    private void setupDeleteFieldButton() {
        if (wordView.getPermissionService().hasWordWritePermission()) {
            wordTextFieldForm.getDelete().setVisible(true);
        }
        wordTextFieldForm.getDelete().addClickListener(event -> {
            commonDialog.getDialog().open();
            setVisibleFirstRightButtonToFalse();
            setupDeleteButton();
            setupCancelButtton();
        });
        wordTextFieldForm.getDetail().setReadOnly(true);
    }

    private void setupCancelButtton() {
        commonDialog.getLeftButton().setText(getTranslation("cancel"));
        commonDialog.getLeftButton().addClickListener(dialogEvent -> commonDialog.getDialog().close());
    }

    private void setupDeleteButton() {
        getDeleteButton().setText(getTranslation("delete"));
        getDeleteButton().addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
        getDeleteButton()
                .addClickListener(dialogEvent -> {
                    try {
                        openWordDialog();
                        executeRemoveOperation();
                        closeDialog();
                        refresh(wordView, wordFacade);
                        showSuccess(getTranslation("delete.success.message"));
                    } catch (RuntimeException exception) {
                        showNotification(exception.getMessage());
                    }
                });
    }

    private Button getDeleteButton() {
        return commonDialog.getSecondRightButton();
    }

    private void setVisibleFirstRightButtonToFalse() {
        commonDialog.getFirstRightButton().setVisible(false);
    }

    protected void createWordDialog() {
        commonDialog = new CommonDialog(getConfirmationMessage(), getDescription());
    }

    protected abstract void createBinder();

    protected abstract void bind();

    protected abstract H4 getConfirmationMessage();

    protected abstract void executeRemoveOperation();
}

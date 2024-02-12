package com.example.dictionary.ui.words.dictionary;

import com.example.dictionary.application.dto.DictionaryDto;
import com.example.dictionary.application.facade.DictionaryFacade;
import com.example.dictionary.ui.words.common.CommonDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.dictionary.ui.util.UiUtils.getAddButton;
import static com.example.dictionary.ui.util.UiUtils.getCancelButton;
import static com.example.dictionary.ui.util.UiUtils.getResetButton;
import static com.example.dictionary.ui.util.UiUtils.getSaveButton;
import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

public class DictionarySelector extends Div {


    private ComboBox<DictionaryDto> dictionary;

    private HorizontalLayout sourceLayout;

    private DictionaryForm dictionaryForm;

    private CommonDialog dictionaryDialogForm;

    private Dialog dictionaryDialog;

    private final DictionaryFacade dictionaryFacade;

    private List<DictionaryDto> allDictionaries;

    private BeanValidationBinder<?> binder;

    private Set<DictionaryDto> dictionaries;

    public DictionarySelector(DictionaryFacade dictionaryFacade,
                              BeanValidationBinder<?> binder,
                              Set<DictionaryDto> dictionaries) {
        this.dictionaryFacade = dictionaryFacade;
        this.binder = binder;
        this.dictionaries = dictionaries;

        setupSourceField();
    }

    public void setupSourceField() {
        dictionary = new ComboBox<>(getTranslation("word.sources"));
        dictionary.setRequired(true);
        allDictionaries = new ArrayList<>(dictionaryFacade.getAllDictionaries());
        dictionary.setItems(allDictionaries);
        dictionary.setItemLabelGenerator(DictionaryDto::getName);
        binder.forField(dictionary)
                .asRequired(getTranslation("dictionary.name.not.null.error.message"))
                .bind(binder -> dictionaries.stream()
                                .filter(dictionaryDto -> dictionaryDto.getName().equals(dictionary.getValue().toString()))
                                .findAny()
                                .orElse(null),
                        ((binder, dictionaryDto) -> dictionaries.add(dictionaryDto)));
        Button addSource = getAddButton();

        dictionaryForm = new DictionaryForm(dictionaryFacade);
        dictionaryDialogForm = new CommonDialog(dictionaryForm, getTranslation("word.source.new"));
        dictionaryDialog = dictionaryDialogForm.getDialog();

        addSource.addClickListener(event -> dictionaryDialogForm.getDialog().open());
        setupDictionaryFormButtons();

        sourceLayout = new HorizontalLayout(dictionary, addSource);
        dictionary.setWidthFull();
        addSource.getStyle().set("border", "solid 1px rgba(0,106,245,0.3)");
        dictionary.setHeight("91px");
        sourceLayout.setVerticalComponentAlignment(CENTER, dictionary, addSource);
        add(sourceLayout, dictionaryDialog);
    }

    private void setupDictionaryFormButtons() {
        setupSaveButton();
        setupCancelButton();
        setupResetButton();
    }

    private void setupSaveButton() {
        Button saveButton = getSaveButton(dictionaryDialogForm);
        saveButton.addClickListener(event -> {
            try {
                dictionaryForm.saveDictionary();
                dictionary.setItems(dictionaryFacade.getAllDictionaries());
                dictionary.setValue(dictionaryFacade.getDictionaryByName(dictionaryForm.getName()));
                showSuccess(getTranslation("add.success.message", dictionaryForm.getName()));
                refreshSourceForm();
            } catch (ValidationException ignored) {
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });
    }

    private void setupCancelButton() {
        Button cancelButton = getCancelButton(dictionaryDialogForm);
        cancelButton.addClickListener(event -> {
            refreshSourceForm();
        });
    }

    private void setupResetButton() {
        Button resetButton = getResetButton(dictionaryDialogForm);
        resetButton.addClickListener(event -> dictionaryForm.reset());
    }

    private void refreshSourceForm() {
        dictionaryDialog.close();
        dictionaryForm.reset();
    }

    public ComboBox<DictionaryDto> getDictionary() {
        return dictionary;
    }

    public BeanValidationBinder<?> getBinder() {
        return binder;
    }

    public Set<DictionaryDto> getDictionaries() {
        return dictionaries;
    }
}

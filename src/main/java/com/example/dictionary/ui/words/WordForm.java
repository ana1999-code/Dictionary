package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.CategoryFacade;
import com.example.dictionary.application.facade.WordFacade;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static com.example.dictionary.ui.util.UiUtils.getCloseButton;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END;

public class WordForm extends FormLayout {

    private TextField name;

    private ComboBox<CategoryDto> category;

    private VerticalLayout definitionsLayout;

    private VerticalLayout examplesLayout;

    private BeanValidationBinder<WordDto> wordBinder;

    private final WordFacade wordFacade;

    private final CategoryFacade categoryFacade;

    private List<CategoryDto> allCategories;

    private WordDto word;

    private static int nrOfDefinitions;

    public WordForm(WordFacade wordFacade, CategoryFacade categoryFacade) {
        this.wordFacade = wordFacade;
        this.categoryFacade = categoryFacade;

        setupNameField();
        setupForm();
    }

    private void setupForm() {
        word = new WordDto();
        wordBinder = new BeanValidationBinder<>(WordDto.class);
        setupCategoryField();

        wordBinder.bindInstanceFields(this);
        setupDefAndExampleLayouts();
    }

    private void setupNameField() {
        name = new TextField("Word");
        name.setRequired(true);
        add(name);
    }

    private void setupCategoryField() {
        category = new ComboBox<>("Category");
        category.setAllowCustomValue(true);
        category.setRequired(true);
        allCategories = new ArrayList<>(categoryFacade.getAllCategories());
        category.setItems(allCategories);
        category.setItemLabelGenerator(CategoryDto::getName);

        category.addCustomValueSetListener(event -> {
            String customCategoryName = event.getDetail();
            handleCustomCategoryValue(customCategoryName);
        });
        wordBinder.forField(category)
                .asRequired("Category must not be empty")
                .bind(WordDto::getCategory, WordDto::setCategory);

        add(category);
    }

    private void setupDefAndExampleLayouts() {
        definitionsLayout = new VerticalLayout();
        definitionsLayout.add(new NativeLabel("Definitions"));

        examplesLayout = new VerticalLayout();

        setColspan(definitionsLayout, 2);
        setColspan(examplesLayout, 2);

        setupDefinitionFields();
        setupExamplesLayout();

        add(definitionsLayout, examplesLayout);
    }

    private void setupDefinitionFields() {
        TextField definition = new TextField();
        definition.setWidthFull();
        definition.addValueChangeListener(event -> {
            wordBinder.forField(definition)
                    .bind(wordDto -> "",
                            (wordDto, definitionValue) -> wordDto.getDefinitions()
                                    .add(new DefinitionDto(definitionValue)));
            definition.setReadOnly(true);

            setupDefinitionFields();
        });
        nrOfDefinitions++;

        Button deleteDefinition = getCloseButton();
        deleteDefinition.setEnabled(false);

        HorizontalLayout definitionLayout = new HorizontalLayout(definition, deleteDefinition);
        definitionLayout.setWidthFull();
        definitionLayout.setDefaultVerticalComponentAlignment(END);

        enableDeleteDefinitionButton(nrOfDefinitions > 1);
        deleteDefinition.addClickListener(event -> {
            definitionsLayout.remove(definitionLayout);
            wordBinder.forField(definition)
                    .bind(wordDto -> "",
                            (wordDto, definitionValue) -> wordDto.getDefinitions()
                                    .remove(new DefinitionDto(definitionValue)));

            nrOfDefinitions--;
            enableDeleteDefinitionButton(nrOfDefinitions == 1);
        });

        definitionsLayout.add(definitionLayout);
    }

    private void setupExamplesLayout() {
        NativeLabel examplesLabel = new NativeLabel("Examples");

        Button addExample = new Button(new Icon(VaadinIcon.PLUS));
        addExample.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addExample.addClickListener(event -> setupExampleFields());

        HorizontalLayout examplesHeader = new HorizontalLayout(examplesLabel, addExample);
        examplesHeader.setDefaultVerticalComponentAlignment(CENTER);

        examplesLayout.add(examplesHeader);
    }

    private void setupExampleFields() {
        TextField example = new TextField();
        example.setWidthFull();

        example.addValueChangeListener(event -> {
                    wordBinder.forField(example)
                            .bind(wordDto -> "",
                                    (wordDto, exampleValue) -> wordDto
                                            .addExample(new ExampleDto(exampleValue)));
                    example.setReadOnly(true);
                }
        );

        Button deleteExample = getCloseButton();
        HorizontalLayout exampleLayout = new HorizontalLayout(example, deleteExample);
        exampleLayout.setWidthFull();
        exampleLayout.setDefaultVerticalComponentAlignment(END);

        deleteExample.addClickListener(event -> {
            examplesLayout.remove(exampleLayout);
            wordBinder.forField(example)
                    .bind(wordDto -> "",
                            (wordDto, exampleValue) -> wordDto
                                    .getExamples()
                                    .remove(new ExampleDto(exampleValue)));
        });

        examplesLayout.add(exampleLayout);
    }

    private void enableDeleteDefinitionButton(boolean enabled) {
        definitionsLayout.getChildren()
                .forEach(component -> {
                    for (Component comp : component.getChildren().toList()) {
                        if (comp instanceof Button) {
                            ((Button) comp).setEnabled(enabled);
                        }
                    }
                });
    }

    private void handleCustomCategoryValue(String customCategoryName) {
        CategoryDto existingCategory = allCategories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(customCategoryName))
                .findFirst()
                .orElse(null);

        if (existingCategory == null) {
            CategoryDto newCategory = new CategoryDto();
            newCategory.setName(customCategoryName);

            allCategories.add(newCategory);
            category.setItems(allCategories);
        }
        category.setValue(existingCategory != null ?
                existingCategory : new CategoryDto(customCategoryName));
    }

    protected void saveWord() throws ValidationException {
        wordBinder.writeBean(word);
        wordFacade.addWord(word);
    }

    public void reset() {
        name.clear();
        remove(category, definitionsLayout, examplesLayout);
        setupForm();
    }

    public String getName() {
        return name.getValue();
    }
}

package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.MainLayout;
import com.example.dictionary.ui.words.operation.AddAntonymOperation;
import com.example.dictionary.ui.words.operation.AddDefinitionOperation;
import com.example.dictionary.ui.words.operation.AddExampleOperation;
import com.example.dictionary.ui.words.operation.AddSynonymOperation;
import com.example.dictionary.ui.words.operation.WordOperationTemplate;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Set;

import static com.example.dictionary.ui.util.UiUtils.WIDTH;
import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.icon.VaadinIcon.ARROW_LEFT;

@Route(value = "word/:wordName?", layout = MainLayout.class)
@PermitAll
public class WordView extends VerticalLayout implements HasUrlParameter<String> {

    private final WordFacade wordFacade;

    private WordDto word;

    private TextField name = new TextField("Name");

    private TextField category = new TextField("Category");

    private Binder<WordDto> wordBinder = new Binder<>(WordDto.class);

    private VerticalLayout definitionLayout = new VerticalLayout();

    private VerticalLayout exampleLayout = new VerticalLayout();

    private VerticalLayout synonymLayout = new VerticalLayout();

    private VerticalLayout antonymLayout = new VerticalLayout();

    private Button addDefinition = new Button(new Icon(VaadinIcon.PLUS));

    private Button addExample = new Button(new Icon(VaadinIcon.PLUS));

    private Button addSynonym = new Button(new Icon(VaadinIcon.PLUS));

    private Button addAntonym = new Button(new Icon(VaadinIcon.PLUS));

    private Button close = new Button();

    private Button delete = new Button("Delete Word");

    public WordView(WordFacade wordFacade) {
        this.wordFacade = wordFacade;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        word = wordFacade.getWordByName(beforeEvent.getRouteParameters().get("wordName").get());
        wordBinder.setBean(word);

        name.setReadOnly(true);
        category.setReadOnly(true);

        wordBinder.bind(name, "name");
        wordBinder.bind(category, "category.name");
        setupButtons();

        setupDefinitions();
        setupExamples();
        setupSynonyms();
        setupAntonyms();
        setupAddDefinitionButton();
        setupAddExampleButton();
        setupAddSynonymButton();
        setupAddAntonymButton();
        HorizontalLayout layout = new HorizontalLayout(name, category);
        HorizontalLayout layout2 = new HorizontalLayout(definitionLayout, exampleLayout);
        HorizontalLayout layout1 = new HorizontalLayout(synonymLayout, antonymLayout);
        layout2.setWidth(WIDTH);
        layout1.setWidth(WIDTH);

        definitionLayout.setSpacing(false);
        exampleLayout.setSpacing(false);
        synonymLayout.setSpacing(false);
        antonymLayout.setSpacing(false);
        definitionLayout.setWidth(WIDTH);
        exampleLayout.setWidth(WIDTH);
        synonymLayout.setWidth(WIDTH);
        antonymLayout.setWidth(WIDTH);


        add(layout, layout2, layout1);
    }

    private void setupButtons() {
        setupCancelButton();
        setupDeleteButton();

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(close, delete);
        buttonsLayout.getStyle().set("flex-wrap", "wrap");
        buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
        close.getStyle().set("margin-inline-end", "auto");
        buttonsLayout.setWidth(WIDTH);
        add(buttonsLayout);
    }

    private void setupCancelButton() {
        close.setIcon(new Icon(ARROW_LEFT));
        close.addClickListener(event -> UI.getCurrent().navigate(WordsView.class));
    }

    private void setupDeleteButton() {
        delete.addThemeVariants(LUMO_ERROR);
        delete.addClickListener(event -> {
            try {
                String wordValue = name.getValue();
                WordDialog dialog = new WordDialog(
                        new H4("Are you sure you want to delete word [%s]?".formatted(wordValue)),
                        "Delete word");
                dialog.getDialog().open();
                dialog.getFirstRightButton().setVisible(false);
                dialog.getSecondRightButton().setText("Delete");
                dialog.getSecondRightButton().addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
                dialog.getSecondRightButton()
                        .addClickListener(dialogEvent -> {
                            wordFacade.deleteWordByName(wordValue);
                            dialog.getDialog().close();
                            UI.getCurrent().navigate(WordsView.class);
                            showSuccess("Successfully Removed");
                        });
                dialog.getLeftButton().setText("Cancel");
                dialog.getLeftButton().addClickListener(dialogEvent -> dialog.getDialog().close());
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });
    }

    private void setupDefinitions() {
        Set<DefinitionDto> definitions = word.getDefinitions();
        HorizontalLayout layout = new HorizontalLayout(new H5("Definitions"), addDefinition);
        layout.setDefaultVerticalComponentAlignment(Alignment.END);
        definitionLayout.add(layout);
        definitions.forEach(this::setupDefinitionsLayout);
    }

    private void setupExamples() {
        Set<ExampleDto> examples = word.getExamples();
        HorizontalLayout layout = new HorizontalLayout(new H5("Examples"), addExample);
        layout.setDefaultVerticalComponentAlignment(Alignment.END);
        exampleLayout.add(layout);
        examples.forEach(this::setupExamplesLayout);
    }

    private void setupSynonyms() {
        Set<WordDto> examples = word.getSynonyms();
        HorizontalLayout layout = new HorizontalLayout(new H5("Synonyms"), addSynonym);
        layout.setDefaultVerticalComponentAlignment(Alignment.END);
        synonymLayout.add(layout);
        examples.forEach(this::setupSynonymLayout);
    }

    private void setupAntonyms() {
        Set<WordDto> examples = word.getAntonyms();
        HorizontalLayout layout = new HorizontalLayout(new H5("Antonyms"), addAntonym);
        layout.setDefaultVerticalComponentAlignment(Alignment.END);
        antonymLayout.add(layout);
        examples.forEach(this::setupAntonymLayout);
    }

    private void setupDefinitionsLayout(DefinitionDto definitionDto) {
        Binder<DefinitionDto> definitionBinder = new Binder<>(DefinitionDto.class);
        WordTextFieldForm text = new WordTextFieldForm();
        text.getDelete().setVisible(true);
        text.getDelete().addClickListener(event -> {
            try {
                String definitionValue = text.getDetail().getValue();
                WordDialog dialog = new WordDialog(
                        new H4("Are you sure you want to delete definition [%s]?".formatted(definitionValue)),
                        "Delete definition");
                dialog.getDialog().open();
                dialog.getFirstRightButton().setVisible(false);
                dialog.getSecondRightButton().setText("Delete");
                dialog.getSecondRightButton().addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
                dialog.getSecondRightButton()
                        .addClickListener(dialogEvent -> {
                            wordFacade.removeDefinitionFromWord(name.getValue(), definitionDto);
                            dialog.getDialog().close();
                            refresh();
                            showSuccess("Successfully Removed");
                        });
                dialog.getLeftButton().setText("Cancel");
                dialog.getLeftButton().addClickListener(dialogEvent -> dialog.getDialog().close());
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });
        text.getDetail().setReadOnly(true);
        definitionBinder.setBean(definitionDto);
        definitionBinder.bind(text.getDetail(), "text");
        definitionLayout.add(text);
    }

    private void setupExamplesLayout(ExampleDto exampleDto) {
        Binder<ExampleDto> exampleBinder = new Binder<>(ExampleDto.class);
        WordTextFieldForm text = new WordTextFieldForm();
        text.getDelete().setVisible(true);
        text.getDelete().addClickListener(event -> {
            try {
                String exampleValue = text.getDetail().getValue();
                WordDialog dialog = new WordDialog(
                        new H4("Are you sure you want to delete example [%s]?".formatted(exampleValue)),
                        "Delete example");
                dialog.getDialog().open();
                dialog.getFirstRightButton().setVisible(false);
                dialog.getSecondRightButton().setText("Delete");
                dialog.getSecondRightButton().addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
                dialog.getSecondRightButton()
                        .addClickListener(dialogEvent -> {
                            wordFacade.removeExampleFromWord(name.getValue(), exampleDto);
                            dialog.getDialog().close();
                            refresh();
                            showSuccess("Successfully Removed");
                        });
                dialog.getLeftButton().setText("Cancel");
                dialog.getLeftButton().addClickListener(dialogEvent -> dialog.getDialog().close());
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });
        text.getDetail().setReadOnly(true);
        exampleBinder.setBean(exampleDto);
        exampleBinder.bind(text.getDetail(), "text");
        exampleLayout.add(text);
    }

    private void setupSynonymLayout(WordDto synonym) {
        Binder<WordDto> synonymBinder = new Binder<>(WordDto.class);
        WordTextFieldForm text = new WordTextFieldForm();
        text.getDelete().setVisible(true);
        text.getDelete().addClickListener(event -> {
            try {
                String wordValue = text.getDetail().getValue();
                WordDialog dialog = new WordDialog(
                        new H4("Are you sure you want to delete synonym [%s]?".formatted(wordValue)),
                        "Delete synonym");
                dialog.getDialog().open();
                dialog.getFirstRightButton().setVisible(false);
                dialog.getSecondRightButton().setText("Delete");
                dialog.getSecondRightButton().addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
                dialog.getSecondRightButton()
                        .addClickListener(dialogEvent -> {
                            wordFacade.removeSynonym(name.getValue(), synonym);
                            dialog.getDialog().close();
                            refresh();
                            showSuccess("Successfully Removed");
                        });
                dialog.getLeftButton().setText("Cancel");
                dialog.getLeftButton().addClickListener(dialogEvent -> dialog.getDialog().close());
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });
        text.getDetail().setReadOnly(true);
        synonymBinder.setBean(synonym);
        synonymBinder.bind(text.getDetail(), "name");
        synonymLayout.add(text);
    }

    private void setupAntonymLayout(WordDto antonym) {
        Binder<WordDto> antonymBinder = new Binder<>(WordDto.class);
        WordTextFieldForm text = new WordTextFieldForm();
        text.getDelete().setVisible(true);
        text.getDelete().addClickListener(event -> {
            try {
                String wordValue = text.getDetail().getValue();
                WordDialog dialog = new WordDialog(
                        new H4("Are you sure you want to delete antonym [%s]?".formatted(wordValue)),
                        "Delete antonym");
                dialog.getDialog().open();
                dialog.getFirstRightButton().setVisible(false);
                dialog.getSecondRightButton().setText("Delete");
                dialog.getSecondRightButton().addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
                dialog.getSecondRightButton()
                        .addClickListener(dialogEvent -> {
                            wordFacade.removeAntonym(name.getValue(), antonym);
                            dialog.getDialog().close();
                            refresh();
                            showSuccess("Successfully Removed");
                        });
                dialog.getLeftButton().setText("Cancel");
                dialog.getLeftButton().addClickListener(dialogEvent -> dialog.getDialog().close());
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });
        text.getDetail().setReadOnly(true);
        antonymBinder.setBean(antonym);
        antonymBinder.bind(text.getDetail(), "name");
        antonymLayout.add(text);
    }

    private void setupAddSynonymButton() {
        addSynonym.addClickListener(event -> {
            WordOperationTemplate wordOperationTemplate =
                    new AddSynonymOperation(wordFacade, name.getValue());
            wordOperationTemplate.execute();
            refresh();
        });
    }

    private void setupAddAntonymButton() {
        addAntonym.addClickListener(event -> {
            WordOperationTemplate wordOperationTemplate =
                    new AddAntonymOperation(wordFacade, name.getValue());
            wordOperationTemplate.execute();
            refresh();
        });
    }

    private void setupAddDefinitionButton() {
        addDefinition.addClickListener(event -> {
            WordOperationTemplate wordOperationTemplate =
                    new AddDefinitionOperation(wordFacade, name.getValue());
            wordOperationTemplate.execute();
            refresh();
        });
    }

    private void setupAddExampleButton() {
        addExample.addClickListener(event -> {
            WordOperationTemplate wordOperationTemplate =
                    new AddExampleOperation(wordFacade, name.getValue());
            wordOperationTemplate.execute();
            refresh();
        });
    }

    private void refresh() {
        word = wordFacade.getWordByName(name.getValue());
        definitionLayout.removeAll();
        exampleLayout.removeAll();
        synonymLayout.removeAll();
        antonymLayout.removeAll();
        setupDefinitions();
        setupExamples();
        setupSynonyms();
        setupAntonyms();
    }
}

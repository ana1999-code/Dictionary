package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.application.security.util.SecurityUtils;
import com.example.dictionary.ui.MainLayout;
import com.example.dictionary.ui.security.CurrentUserPermissionService;
import com.example.dictionary.ui.words.operation.add.AddOperationTemplate;
import com.example.dictionary.ui.words.operation.add.comments.AddCommentOperation;
import com.example.dictionary.ui.words.operation.add.detail.AddAntonymOperation;
import com.example.dictionary.ui.words.operation.add.detail.AddDefinitionOperation;
import com.example.dictionary.ui.words.operation.add.detail.AddExampleOperation;
import com.example.dictionary.ui.words.operation.add.detail.AddSynonymOperation;
import com.example.dictionary.ui.words.operation.remove.RemoveOperationTemplate;
import com.example.dictionary.ui.words.operation.remove.comment.RemoveCommentOperation;
import com.example.dictionary.ui.words.operation.remove.detail.RemoveAntonymOperation;
import com.example.dictionary.ui.words.operation.remove.detail.RemoveDefinitionOperation;
import com.example.dictionary.ui.words.operation.remove.detail.RemoveExampleOperation;
import com.example.dictionary.ui.words.operation.remove.detail.RemoveSynonymOperation;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.dictionary.ui.util.UiUtils.WIDTH;
import static com.example.dictionary.ui.util.UiUtils.getAddButton;
import static com.example.dictionary.ui.util.UiUtils.getAvatar;
import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.icon.VaadinIcon.ARROW_LEFT;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

@Route(value = "word/:wordName?", layout = MainLayout.class)
@PermitAll
public class WordView extends VerticalLayout implements HasUrlParameter<String> {

    private final CurrentUserPermissionService permissionService;

    private final WordFacade wordFacade;

    private WordDto word;

    private TextField name = new TextField("Name");

    private TextField category = new TextField("Category");

    private Binder<WordDto> wordBinder = new Binder<>(WordDto.class);

    private VerticalLayout definitionLayout = new VerticalLayout();

    private VerticalLayout exampleLayout = new VerticalLayout();

    private VerticalLayout synonymLayout = new VerticalLayout();

    private VerticalLayout antonymLayout = new VerticalLayout();

    private VerticalLayout commentLayout = new VerticalLayout();

    private Button addDefinition = getAddButton();

    private Button addExample = getAddButton();

    private Button addSynonym = getAddButton();

    private Button addAntonym = getAddButton();

    private Button addComment = getAddButton();

    private Button back = new Button();

    private Button delete = new Button("Delete Word");

    public WordView(CurrentUserPermissionService permissionService, WordFacade wordFacade) {
        this.permissionService = permissionService;
        this.wordFacade = wordFacade;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        word = wordFacade.getWordByName(beforeEvent.getRouteParameters().get("wordName").get());

        HorizontalLayout nameAndCategoryLayout = getNameAndCategoryLayout();
        HorizontalLayout definitionAndExampleLayout = getDefinitionAndExampleLayout();
        HorizontalLayout synonymAndAntonymLayout = getSynonymAndAntonymLayout();
        HorizontalLayout commentsLayout = getCommentsHorizontalLayout();

        setupWordBinder();
        setupBackAndDeleteButtons();
        setupWordDetails();

        setupLayoutsStyle(List.of(
                definitionLayout,
                exampleLayout,
                synonymLayout,
                antonymLayout,
                commentLayout)
        );

        if (permissionService.hasWordWritePermission()) {
            setupAddWordDetailButtons();
        }
        setupAddCommentButton();
        add(nameAndCategoryLayout, definitionAndExampleLayout, synonymAndAntonymLayout, commentsLayout);
    }

    private void setupLayoutsStyle(List<VerticalLayout> layouts) {
        layouts.forEach(
                layout -> layout.getStyle()
                        .set("border", "0.5px ridge rgba(0,95,219,0.39)")
                        .set("border-radius", "5px")
        );
    }

    private void setupAddWordDetailButtons() {
        setupAddDefinitionButton();
        setupAddExampleButton();
        setupAddSynonymButton();
        setupAddAntonymButton();
    }

    public void setupWordDetails() {
        setupDefinitionsLayout();
        setupExamplesLayout();
        setupSynonymsLayout();
        setupAntonymsLayout();
        setupCommentsLayout();
    }

    private HorizontalLayout getNameAndCategoryLayout() {
        HorizontalLayout nameAndCategoryLayout = new HorizontalLayout(name, category);
        return nameAndCategoryLayout;
    }

    private HorizontalLayout getDefinitionAndExampleLayout() {
        HorizontalLayout definitionAndExampleLayout = new HorizontalLayout(definitionLayout, exampleLayout);
        definitionAndExampleLayout.setWidth(WIDTH);
        definitionLayout.setSpacing(false);
        exampleLayout.setSpacing(false);
        definitionLayout.setWidth(WIDTH);
        exampleLayout.setWidth(WIDTH);
        return definitionAndExampleLayout;
    }

    private HorizontalLayout getSynonymAndAntonymLayout() {
        HorizontalLayout synonymAndAntonymLayout = new HorizontalLayout(synonymLayout, antonymLayout);
        synonymAndAntonymLayout.setWidth(WIDTH);
        synonymLayout.setSpacing(false);
        antonymLayout.setSpacing(false);
        synonymLayout.setWidth(WIDTH);
        antonymLayout.setWidth(WIDTH);
        return synonymAndAntonymLayout;
    }

    private HorizontalLayout getCommentsHorizontalLayout() {
        HorizontalLayout commentsLayout = new HorizontalLayout(commentLayout);
        commentsLayout.setWidth(WIDTH);
        commentLayout.setWidthFull();
        return commentsLayout;
    }

    private void setupWordBinder() {
        wordBinder.setBean(word);

        name.setReadOnly(true);
        category.setReadOnly(true);

        wordBinder.bind(name, "name");
        wordBinder.bind(category, "category.name");
    }

    private void setupBackAndDeleteButtons() {
        setupBackButton();

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(back);

        if (permissionService.hasWordWritePermission()) {
            setupDeleteButton();
            buttonsLayout.add(delete);
        }

        buttonsLayout.getStyle().set("flex-wrap", "wrap");
        buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
        back.getStyle().set("margin-inline-end", "auto");
        buttonsLayout.setWidth(WIDTH);

        add(buttonsLayout);
    }

    private void setupBackButton() {
        back.setIcon(new Icon(ARROW_LEFT));
        back.addClickListener(event -> UI.getCurrent().navigate(WordsView.class));
    }

    private void setupDeleteButton() {
        delete.addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);
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

    private void setupDefinitionsLayout() {
        Set<DefinitionDto> definitions = word.getDefinitions();
        HorizontalLayout layout = new HorizontalLayout(new H5("Definitions"));
        if (permissionService.hasWordWritePermission()) {
            layout.add(addDefinition);
        }
        layout.setDefaultVerticalComponentAlignment(CENTER);
        definitionLayout.add(layout);
        definitions.forEach(this::setupDefinitionLayout);
    }

    private void setupExamplesLayout() {
        Set<ExampleDto> examples = word.getExamples();
        HorizontalLayout layout = new HorizontalLayout(new H5("Examples"));
        if (permissionService.hasWordWritePermission()) {
            layout.add(addExample);
        }
        layout.setDefaultVerticalComponentAlignment(CENTER);
        exampleLayout.add(layout);
        examples.forEach(this::setupExampleLayout);
    }

    private void setupSynonymsLayout() {
        Set<WordDto> examples = word.getSynonyms();
        HorizontalLayout layout = new HorizontalLayout(new H5("Synonyms"));
        if (permissionService.hasWordWritePermission()) {
            layout.add(addSynonym);
        }
        layout.setDefaultVerticalComponentAlignment(CENTER);
        synonymLayout.add(layout);
        examples.forEach(this::setupSynonymLayout);
    }

    private void setupAntonymsLayout() {
        Set<WordDto> examples = word.getAntonyms();
        HorizontalLayout layout = new HorizontalLayout(new H5("Antonyms"));
        if (permissionService.hasWordWritePermission()) {
            layout.add(addAntonym);
        }
        layout.setDefaultVerticalComponentAlignment(CENTER);
        antonymLayout.add(layout);
        examples.forEach(this::setupAntonymLayout);
    }

    private void setupCommentsLayout() {
        List<CommentDto> comments = wordFacade.getAllCommentsByWord(name.getValue());
        HorizontalLayout layout = new HorizontalLayout(new H4("Comments"));
        layout.add(addComment);
        layout.setDefaultVerticalComponentAlignment(CENTER);
        commentLayout.add(layout);
        commentLayout.setHorizontalComponentAlignment(CENTER, layout);
        comments.forEach(this::setupCommentLayout);
    }

    private void setupDefinitionLayout(DefinitionDto definitionDto) {
        RemoveOperationTemplate definition =
                new RemoveDefinitionOperation(wordFacade, name.getValue(), definitionDto, this);
        definition.execute();
        definitionLayout.add(definition.getWordTextFieldForm());
    }

    private void setupExampleLayout(ExampleDto exampleDto) {
        RemoveOperationTemplate example =
                new RemoveExampleOperation(wordFacade, name.getValue(), exampleDto, this);
        example.execute();
        exampleLayout.add(example.getWordTextFieldForm());
    }

    private void setupSynonymLayout(WordDto synonymDto) {
        RemoveOperationTemplate synonym =
                new RemoveSynonymOperation(wordFacade, name.getValue(), synonymDto, this);
        synonym.execute();
        synonym.getWordTextFieldForm().getDetail().setHeight("2.25em");
        synonymLayout.add(synonym.getWordTextFieldForm());
    }

    private void setupAntonymLayout(WordDto antonymDto) {
        RemoveOperationTemplate antonym =
                new RemoveAntonymOperation(wordFacade, name.getValue(), antonymDto, this);
        antonym.execute();
        antonym.getWordTextFieldForm().getDetail().setHeight("2.25em");
        antonymLayout.add(antonym.getWordTextFieldForm());
    }

    private void setupCommentLayout(CommentDto commentDto) {
        RemoveOperationTemplate comment =
                new RemoveCommentOperation(wordFacade, name.getValue(), commentDto, this);
        comment.execute();
        UserDto commenter = commentDto.getCommenter();
        String firstName = commenter.getFirstName();
        String lastName = commenter.getLastName();
        Avatar avatar = getAvatar(firstName, lastName);
        Span name = new Span(firstName + " " + lastName);
        LocalDateTime commentedAt = commentDto.getCommentedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
        Span commentedDate = new Span(formatter.format(commentedAt));
        commentedDate.getStyle().set("color", "grey")
                .set("font-size", "0.8em");
        HorizontalLayout commenterLayout =
                new HorizontalLayout(avatar, name, commentedDate);
        commenterLayout.setDefaultVerticalComponentAlignment(CENTER);
        WordTextFieldForm wordTextFieldForm = comment.getWordTextFieldForm();
        wordTextFieldForm.getDelete()
                .setVisible(Objects.requireNonNull(
                        SecurityUtils.getUsername()).equalsIgnoreCase(commenter.getEmail()
                ));

        wordTextFieldForm.setColspan(wordTextFieldForm.getFormLayout(), 2);
        wordTextFieldForm.getDetail().setWidth("95%");
        commentLayout.add(
                commenterLayout,
                wordTextFieldForm
        );
    }

    private void setupAddSynonymButton() {
        addSynonym.addClickListener(event -> {
            AddOperationTemplate addOperationTemplate =
                    new AddSynonymOperation(wordFacade, name.getValue(), this);
            addOperationTemplate.execute();
        });
    }

    private void setupAddAntonymButton() {
        addAntonym.addClickListener(event -> {
            AddOperationTemplate addOperationTemplate =
                    new AddAntonymOperation(wordFacade, name.getValue(), this);
            addOperationTemplate.execute();
        });
    }

    private void setupAddDefinitionButton() {
        addDefinition.addClickListener(event -> {
            AddOperationTemplate addOperationTemplate =
                    new AddDefinitionOperation(wordFacade, name.getValue(), this);
            addOperationTemplate.execute();
        });
    }

    private void setupAddExampleButton() {
        addExample.addClickListener(event -> {
            AddOperationTemplate addOperationTemplate =
                    new AddExampleOperation(wordFacade, name.getValue(), this);
            addOperationTemplate.execute();
        });
    }

    private void setupAddCommentButton() {
        addComment.addClickListener(event -> {
            AddOperationTemplate addOperationTemplate =
                    new AddCommentOperation(wordFacade, name.getValue(), this);
            addOperationTemplate.execute();
        });
    }

    public TextField getName() {
        return name;
    }

    public void setWord(WordDto word) {
        this.word = word;
    }

    public VerticalLayout getDefinitionLayout() {
        return definitionLayout;
    }

    public VerticalLayout getExampleLayout() {
        return exampleLayout;
    }

    public VerticalLayout getSynonymLayout() {
        return synonymLayout;
    }

    public VerticalLayout getAntonymLayout() {
        return antonymLayout;
    }

    public VerticalLayout getCommentLayout() {
        return commentLayout;
    }

    public CurrentUserPermissionService getPermissionService() {
        return permissionService;
    }
}

package com.example.dictionary.ui.quiz;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.domain.repository.WordRepository;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;

@Route(value = "quiz", layout = MainLayout.class)
@PermitAll
@PageTitle("Quiz | " + APP_NAME)
public class QuizView extends VerticalLayout {

    private final WordFacade wordFacade;

    private final WordRepository wordRepository;

    private List<WordDto> words;

    private H4 title;

    private List<RadioButtonGroup> radioButtonGroupList;

    private Button submit;

    private Button reset;

    private VerticalLayout definitionsGroupLayout = new VerticalLayout();

    private static Random RANDOM = new Random();

    public QuizView(WordFacade wordFacade, WordRepository wordRepository) {
        this.wordFacade = wordFacade;
        words = wordFacade.getAllWords();
        this.wordRepository = wordRepository;

        setupTitle();
        setupDefinitionsGroupList();
        setupSubmitButton();
        setupResetButton();

        HorizontalLayout buttons = new HorizontalLayout(submit, reset);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        definitionsGroupLayout.getStyle().set("width", "max-content");
        add(title, definitionsGroupLayout, buttons);
    }

    private void setupResetButton() {
        reset = new Button("Refresh");
        reset.addClickListener(event -> UI.getCurrent().getPage().reload());
    }

    private void setupSubmitButton() {
        submit = new Button("Submit");
        submit.addThemeVariants(LUMO_PRIMARY);
        submit.addClickListener(event -> radioButtonGroupList.forEach(
                radioButtonGroup -> radioButtonGroup.getHelperComponent().setVisible(true)));
    }

    private void setupDefinitionsGroupList() {
        radioButtonGroupList = new ArrayList<>();
        List<WordDto> randomWords = getRandomWords(new ArrayList<>(words), 5);

        for (WordDto wordDto : randomWords) {
            List<String> wordDefinitions = wordRepository.getWordDefinitions(wordDto.getName());
            String definition = getDefinition(wordDefinitions);

            List<String> wordNonDefinitions = wordRepository.getDefinitionsNotIncludedForWord(wordDto.getName());
            String nonDefinitionOne = getDefinition(wordNonDefinitions);
            wordNonDefinitions.remove(nonDefinitionOne);
            String nonDefinitionTwo = getDefinition(wordNonDefinitions);
            wordNonDefinitions.remove(nonDefinitionTwo);
            String nonDefinitionThree = getDefinition(wordNonDefinitions);
            wordNonDefinitions.remove(nonDefinitionThree);
            RadioButtonGroup<String> wordDefinitionsChoices = new RadioButtonGroup<>();

            wordDefinitionsChoices.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
            wordDefinitionsChoices.setLabel(wordDto.getName());
            Set<String> defs = Set.of(definition, nonDefinitionOne, nonDefinitionTwo, nonDefinitionThree);
            wordDefinitionsChoices.setItems(defs);

            wordDefinitionsChoices.addValueChangeListener(ev -> {
                if (ev.getValue().equalsIgnoreCase(definition)) {
                    wordDefinitionsChoices.setHelperComponent(getCorrect());
                } else {
                    wordDefinitionsChoices.setHelperComponent(getIncorrect());
                }
                wordDefinitionsChoices.getHelperComponent().setVisible(false);
            });
            radioButtonGroupList.add(wordDefinitionsChoices);
            definitionsGroupLayout.add(wordDefinitionsChoices);
        }
    }

    private Span getIncorrect() {
        Span incorrect = new Span(createIcon(VaadinIcon.EXCLAMATION_CIRCLE_O),
                new Span("Incorrect"));
        incorrect.getElement().getThemeList().add("badge error");
        return incorrect;
    }

    private Span getCorrect() {
        Span correct = new Span(createIcon(VaadinIcon.CHECK),
                new Span("Correct"));
        correct.getElement().getThemeList().add("badge success");
        return correct;
    }

    private static String getDefinition(List<String> definitions) {
        String definition = definitions.get(RANDOM.nextInt(definitions.size()));
        definitions.remove(definition);
        return definition;
    }

    private void setupTitle() {
        title = new H4("Select correct definition for each word");
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }

    private List<WordDto> getRandomWords(List<WordDto> list, int totalItems) {
        Random random = new Random();
        List<WordDto> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = random.nextInt(list.size());
            newList.add(list.get(randomIndex));
            list.remove(randomIndex);
        }
        return newList;
    }
}

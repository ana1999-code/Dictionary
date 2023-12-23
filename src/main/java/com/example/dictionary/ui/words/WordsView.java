package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static com.vaadin.flow.component.grid.ColumnTextAlign.CENTER;

@Route(value = "words", layout = MainLayout.class)
@PermitAll
@PageTitle("Words | " + APP_NAME)
public class WordsView extends VerticalLayout {

    private Grid<WordDto> wordDtoGrid;

    private final WordFacade wordFacade;

    private final UserFacade userFacade;

    private Set<String> userFavoriteWords;

    private TextField searchField;

    private Button uploadFile;

    private Button addWord;

    public WordsView(WordFacade wordFacade, UserFacade userFacade) {
        this.wordFacade = wordFacade;
        this.userFacade = userFacade;

        setupUploadButton();
        setupAddButton();

        setupWordsGrid();
        setupSearchField();
        setupUserFavoriteWords();

        HorizontalLayout tabLayout =
                new HorizontalLayout(searchField, uploadFile, addWord);
        tabLayout.setWidth("70%");
        tabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        setHorizontalComponentAlignment(Alignment.CENTER, tabLayout, wordDtoGrid);
        setSizeFull();
        add(tabLayout, wordDtoGrid);
    }

    private void setupUploadButton() {
        uploadFile = new Button("Upload File");
        uploadFile.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        uploadFile.setIcon(new Icon(VaadinIcon.UPLOAD));
    }

    private void setupAddButton() {
        addWord = new Button("Add Word");
        addWord.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addWord.setIcon(new Icon(VaadinIcon.PLUS));
    }

    private void setupSearchField() {
        searchField = new TextField();
        searchField.setPlaceholder("Search...");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(
                event -> wordDtoGrid.setItems(
                        wordFacade.getAllWords()
                                .stream()
                                .filter(word -> word
                                        .getName()
                                        .toLowerCase()
                                        .startsWith(searchField.getValue().toLowerCase()))
                                .collect(Collectors.toSet())
                ));
        searchField.setSuffixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setWidthFull();
    }

    private void setupUserFavoriteWords() {
        userFavoriteWords = userFacade.getUserProfile()
                .getUserInfo()
                .getFavorites()
                .stream()
                .map(WordDto::getName)
                .collect(Collectors.toSet());
    }

    private void setupWordsGrid() {
        wordDtoGrid = new Grid<>(WordDto.class, false);
        List<WordDto> words = wordFacade.getAllWords();

        wordDtoGrid.setItems(words);
        wordDtoGrid.addColumn(WordDto::getName)
                .setHeader("Word");
        wordDtoGrid.addColumn(wordDto -> wordDto.getCategory().getName())
                .setHeader("Category");
        wordDtoGrid.addColumn(wordDto -> wordDto.getDefinitions().size())
                .setHeader("Nr. Definitions");
        wordDtoGrid.addColumn(wordDto -> wordDto.getSynonyms().size())
                .setHeader("Nr. Synonyms");
        wordDtoGrid.addColumn(wordDto -> wordDto.getAntonyms().size())
                .setHeader("Nr. Antonyms");
        wordDtoGrid.addColumn(wordDto -> wordDto.getExamples().size())
                .setHeader("Nr. Examples");
        wordDtoGrid.addColumn(WordDto::getAddedAt)
                .setHeader("AddedAt");
        wordDtoGrid.getColumns()
                .forEach(col -> col
                        .setSortable(true)
                        .setSortProperty("")
                        .setTextAlign(CENTER)
                        .setAutoWidth(true));
        wordDtoGrid.addComponentColumn(this::getHeartButton)
                .setTextAlign(CENTER)
                .setWidth("7%");

        wordDtoGrid.setAllRowsVisible(true);
        wordDtoGrid.setWidth("70%");
    }

    private Button getHeartButton(WordDto wordDto) {
        Button favButton = new Button();
        AtomicBoolean isClicked = new AtomicBoolean(false);

        isClicked.set(userFavoriteWords.contains(wordDto.getName()));

        favButton.setIcon(updateHeartIcon(isClicked.get()));
        favButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        favButton.addClickListener(event -> {
            isClicked.set(!isClicked.get());
            updateFavoriteWords(wordDto.getName(), isClicked.get());
            favButton.setIcon(updateHeartIcon(isClicked.get()));
        });

        return favButton;
    }

    private void updateFavoriteWords(String wordName, boolean isClicked) {
        if (isClicked) {
            userFacade.addWordToFavorities(wordName);
        } else {
            userFacade.removeWordFromFavorites(wordName);
        }
    }

    private Icon updateHeartIcon(boolean isClicked) {
        if (isClicked) {
            return new Icon(VaadinIcon.HEART);
        } else {
            return new Icon(VaadinIcon.HEART_O);
        }
    }
}

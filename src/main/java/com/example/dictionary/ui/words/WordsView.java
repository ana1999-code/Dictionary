package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;

@Route(value = "words", layout = MainLayout.class)
@PermitAll
@PageTitle("Words | " + APP_NAME)
public class WordsView extends VerticalLayout {

    private Grid<WordDto> wordDtoGrid;

    private final WordFacade wordFacade;

    private List<WordDto> words = new ArrayList<>();

    public WordsView(WordFacade wordFacade) {
        this.wordFacade = wordFacade;
        wordDtoGrid = new Grid<>(WordDto.class, false);
        wordDtoGrid.setAllRowsVisible(true);

        words = wordFacade.getAllWords();

        wordDtoGrid.setItems(words);
        wordDtoGrid.addColumn(WordDto::getName).setHeader("Word");
        wordDtoGrid.addColumn(WordDto::getAddedAt).setHeader("AddedAt");

        add(wordDtoGrid);
    }
}

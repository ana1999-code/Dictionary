package com.example.dictionary.ui.words.operation;

import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;

public class DataRefresher {

    public static void refresh(WordView wordView, WordFacade wordFacade) {
        wordView.setWord(wordFacade.getWordByName(wordView.getName().getValue()));
        wordView.getDefinitionLayout().removeAll();
        wordView.getExampleLayout().removeAll();
        wordView.getSynonymLayout().removeAll();
        wordView.getAntonymLayout().removeAll();
        wordView.getCommentLayout().removeAll();
        wordView.getSourcesLayout().removeAll();
        wordView.setupWordDetails();
    }
}

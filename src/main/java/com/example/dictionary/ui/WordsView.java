package com.example.dictionary.ui;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;

@Route("words")
public class WordsView extends Div {

    private final WordFacade wordFacade;

    private ComponentRenderer<Component, WordDto> personCardRenderer = new ComponentRenderer<>(
            wordDto -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setMargin(true);


                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.getElement().appendChild(
                        ElementFactory.createStrong(wordDto.getName()));

                VerticalLayout contactLayout = new VerticalLayout();
                contactLayout.setSpacing(false);
                contactLayout.setPadding(false);
                contactLayout.add(new Div(new Text(wordDto.getCategory().getName())));

                infoLayout.add(contactLayout);
                cardLayout.add(infoLayout);
                return cardLayout;
            });

    public WordsView(WordFacade wordFacade) {
        this.wordFacade = wordFacade;

        VirtualList<WordDto> wordDtoVirtualList = new VirtualList<>();
        wordDtoVirtualList.setItems(wordFacade.getAllWords());
        wordDtoVirtualList.setRenderer(personCardRenderer);

        add(wordDtoVirtualList);
    }
}

package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import static com.example.dictionary.ui.util.UiUtils.getCloseButton;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END;

@Route(value = "word/:wordName?", layout = MainLayout.class)
@PermitAll
public class WordView extends VerticalLayout implements HasUrlParameter<String> {

    private NativeLabel wordName;

    private final WordFacade wordFacade;

    private WordDto word;

    private static int nrOfDefinitions;
    private VerticalLayout definitionsLayout = new VerticalLayout();
    private BeanValidationBinder<WordDto> wordBinder = new BeanValidationBinder<>(WordDto.class);



    public WordView(WordFacade wordFacade) {
        this.wordFacade = wordFacade;
        setHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        word = wordFacade.getWordByName(beforeEvent.getRouteParameters().get("wordName").get());
        H2 wordName = new H2(word.getName().toUpperCase());
        wordBinder.readBean(word);
        word.getDefinitions().forEach(
                def -> {
                    TextField definition = new TextField();
                    definition.setValue(def.getText());
                    definition.setWidthFull();
                    definition.setReadOnly(true);

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
        );
        add(wordName, definitionsLayout);
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
}

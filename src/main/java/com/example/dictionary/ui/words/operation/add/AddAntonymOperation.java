package com.example.dictionary.ui.words.operation.add;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.ValidationException;

public class AddAntonymOperation extends AddOperationTemplate {

    public AddAntonymOperation(WordFacade wordFacade, String wordName, WordView wordView) {
        super(wordFacade, wordName, wordView);
    }

    @Override
    protected String getDescription() {
        return "Add Antonym";
    }

    @Override
    protected void createBinderAndSave(TextArea detail) throws ValidationException {
        WordDto wordDto = new WordDto();
        wordDto.setName(detail.getValue());
        wordFacade.addAntonym(wordName, wordDto);
    }
}

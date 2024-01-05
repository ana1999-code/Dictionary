package com.example.dictionary.ui.words.operation;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;

public class AddSynonymOperation extends WordOperationTemplate{

    public AddSynonymOperation(WordFacade wordFacade, String wordName) {
        super(wordFacade, wordName);
    }

    @Override
    protected String getDescription() {
        return "Add Synonym";
    }

    @Override
    protected void createBinderAndSave(TextField detail) throws ValidationException {
        WordDto wordDto = new WordDto();
        wordDto.setName(detail.getValue());
        wordFacade.addSynonym(wordName, wordDto);
    }
}

package com.example.dictionary.application.batch.processor;

import com.example.dictionary.application.annotation.ContributeByUser;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.util.WordEntityAssociationUtil;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Word;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class WordValidatingItemProcessor implements ItemProcessor<Word, Word> {

    @Autowired
    private WordValidator wordValidator;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private WordEntityAssociationUtil associationUtil;

    private final List<String> wordNames = new ArrayList<>();

    @Override
    @ContributeByUser
    public Word process(Word word) throws Exception {
        if (wordNames.contains(word.getName())) {
            throw new DuplicateResourceException("Duplicated word in processed file");
        } else {
            wordNames.add(word.getName());
            wordValidator.validate(wordMapper.wordToWordDto(word));
            associationUtil.associateWordWithEntities(word);
            return word;
        }
    }

    public void resetWordNames() {
        wordNames.clear();
    }
}

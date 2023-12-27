package com.example.dictionary.application.batch.processor;

import com.example.dictionary.application.annotation.ContributeByUser;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.util.WordEntityAssociationUtil;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Word;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class WordValidatingItemProcessor implements ItemProcessor<Word, Word> {

    @Autowired
    private WordValidator wordValidator;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private WordEntityAssociationUtil associationUtil;

    @Override
    @ContributeByUser
    public Word process(Word word) throws Exception {
        try {
            wordValidator.validate(wordMapper.wordToWordDto(word));
            associationUtil.associateWordWithEntities(word);
            return word;
        } catch (RuntimeException exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }
}

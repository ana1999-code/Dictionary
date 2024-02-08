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

    @Autowired
    private DuplicatedWordValidator duplicatedWordValidator;

    @Override
    @ContributeByUser
    public Word process(Word word) throws Exception {
        duplicatedWordValidator.validateWordPresence(word.getName());
        wordValidator.validate(wordMapper.wordToWordDto(word));
        associationUtil.associateWordWithEntities(word);
        duplicatedWordValidator.addProcessedWord(word.getName());
        return word;
    }
}


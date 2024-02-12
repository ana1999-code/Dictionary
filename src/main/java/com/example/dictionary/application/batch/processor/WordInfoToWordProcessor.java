package com.example.dictionary.application.batch.processor;

import com.example.dictionary.application.batch.data.WordInfo;
import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Dictionary;
import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.entity.Word;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class WordInfoToWordProcessor implements ItemProcessor<WordInfo, Word> {

    @Override
    public Word process(WordInfo wordInfo) throws Exception {
        Word word = new Word();
        Category category = new Category(wordInfo.getCategory());
        Definition definition = new Definition(wordInfo.getDefinition());
        Example example = new Example(wordInfo.getExample());
        Dictionary dictionary = new Dictionary(wordInfo.getSource(), wordInfo.getUrl());

        word.setName(wordInfo.getName());
        word.setCategory(category);
        word.addDefinition(definition);
        if (example.getText() != null) {
            word.addExample(example);
        }
        word.setAddedAt(LocalDateTime.now());
        word.addDictionary(dictionary);

        return word;
    }
}

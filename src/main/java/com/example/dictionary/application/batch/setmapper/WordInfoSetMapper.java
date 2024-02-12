package com.example.dictionary.application.batch.setmapper;

import com.example.dictionary.application.batch.data.WordInfo;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class WordInfoSetMapper implements FieldSetMapper<WordInfo> {

    @Override
    public WordInfo mapFieldSet(FieldSet fieldSet) throws BindException {
        WordInfo wordInfo = new WordInfo();
        wordInfo.setName(fieldSet.readString("name"));
        wordInfo.setCategory(fieldSet.readString("category"));
        wordInfo.setDefinition(fieldSet.readString("definition"));
        wordInfo.setSource(fieldSet.readString("source"));

        if (!fieldSet.readString("url").isEmpty()) {
            wordInfo.setUrl(fieldSet.readString("url"));
        }
        if (!fieldSet.readString("example").isEmpty()) {
            wordInfo.setExample(fieldSet.readString("example"));
        }

        return wordInfo;
    }
}

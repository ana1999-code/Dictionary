package com.example.dictionary.application.dto;

import java.util.Set;

public class ExampleDto {

    private Integer id;

    private String text;

    private Set<WordDto> words;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<WordDto> getWords() {
        return words;
    }

    public void setWords(Set<WordDto> words) {
        this.words = words;
    }
}

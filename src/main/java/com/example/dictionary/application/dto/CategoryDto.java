package com.example.dictionary.application.dto;

import java.util.Set;

public class CategoryDto {

    private Integer id;

    private String name;

    private Set<WordDto> words;

    public CategoryDto() {
    }

    public CategoryDto(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WordDto> getWords() {
        return words;
    }

    public void setWords(Set<WordDto> words) {
        this.words = words;
    }
}

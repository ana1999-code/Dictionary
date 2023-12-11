package com.example.dictionary.application.dto;

import java.util.HashSet;
import java.util.Set;

public class WordDto {

    private Integer id;

    private String name;

    private final Set<DefinitionDto> definitions = new HashSet<>();

    private final Set<ExampleDto> examples = new HashSet<>();

    private final Set<WordDto> synonyms = new HashSet<>();

    private final Set<WordDto> antonyms = new HashSet<>();

    private CategoryDto category;

    private final Set<UserDto> contributors = new HashSet<>();

    private final Set<CommentDto> comments = new HashSet<>();

    public WordDto() {
    }

    public WordDto(String name, CategoryDto category) {
        this.name = name;
        this.category = category;
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

    public Set<DefinitionDto> getDefinitions() {
        return definitions;
    }

    public Set<ExampleDto> getExamples() {
        return examples;
    }

    public Set<WordDto> getSynonyms() {
        return synonyms;
    }

    public Set<WordDto> getAntonyms() {
        return antonyms;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public Set<UserDto> getContributors() {
        return contributors;
    }

    public Set<CommentDto> getComments() {
        return comments;
    }
}

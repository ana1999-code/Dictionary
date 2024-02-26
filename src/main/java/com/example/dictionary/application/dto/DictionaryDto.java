package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DictionaryDto {

    private Long id;

    @NotNull(message = "{dictionary.name.not.null.error.message}")
    private String name;

    private String url;

    private final List<WordDto> words = new ArrayList<>();

    private final Set<DefinitionDto> definitions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WordDto> getWords() {
        return words;
    }

    public void addWord(WordDto word) {
        words.add(word);
    }

    public Set<DefinitionDto> getDefinitions() {
        return definitions;
    }

    public void addDefinition(DefinitionDto definition) {
        definitions.add(definition);
    }

    @Override
    public String toString() {
        return "DictionaryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

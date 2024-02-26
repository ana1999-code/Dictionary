package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefinitionDto {

    private Integer id;

    @NotEmpty(message = "{word.definition.error.empty}")
    private String text;

    private final Set<DictionaryDto> dictionaries = new HashSet<>();

    public DefinitionDto() {
    }

    public DefinitionDto(String text) {
        this.text = text;
    }

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

    public Set<DictionaryDto> getDictionaries() {
        return dictionaries;
    }

    public void addDictionary(DictionaryDto dictionaryDto) {
        dictionaries.add(dictionaryDto);
    }

    @Override
    public String toString() {
        return "DefinitionDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefinitionDto that = (DefinitionDto) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}

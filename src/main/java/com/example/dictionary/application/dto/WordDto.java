package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class WordDto {

    private Integer id;

    @Pattern(regexp = "^[a-zA-Z]+$",
            message = "{word.error.format}")
    @NotNull(message = "{word.error.empty}")
    private String name;

    private final Set<DefinitionDto> definitions = new HashSet<>();

    private final Set<ExampleDto> examples = new HashSet<>();

    private final Set<WordDto> synonyms = new HashSet<>();

    private final Set<WordDto> antonyms = new HashSet<>();

    @NotNull(message = "{word.category.error.message}")
    private CategoryDto category;

    private final Set<UserDto> contributors = new HashSet<>();

    private final List<CommentDto> comments = new ArrayList<>();

    private LocalDateTime addedAt;

    private final Set<DictionaryDto> dictionaries = new HashSet<>();

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

    public List<CommentDto> getComments() {
        return comments;
    }

    public void addDefinition(DefinitionDto definition) {
        definitions.add(definition);
    }

    public void addExample(ExampleDto example) {
        examples.add(example);
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public Set<DictionaryDto> getDictionaries() {
        return dictionaries;
    }

    @Override
    public String toString() {
        return "WordDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", definitions=" + definitions +
                ", examples=" + examples +
                ", synonyms=" + synonyms +
                ", antonyms=" + antonyms +
                ", category=" + category +
                ", contributors=" + contributors +
                ", comments=" + comments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordDto wordDto = (WordDto) o;
        return Objects.equals(name, wordDto.name)
                && Objects.equals(category, wordDto.category)
                && Objects.equals(addedAt, wordDto.addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, addedAt);
    }
}

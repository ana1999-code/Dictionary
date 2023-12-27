package com.example.dictionary.application.batch.data;

import jakarta.validation.constraints.NotEmpty;

public class WordInfo {

    @NotEmpty
    private String name;

    @NotEmpty
    private String category;

    @NotEmpty
    private String definition;

    private String example;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}

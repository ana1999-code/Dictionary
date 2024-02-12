package com.example.dictionary.application.batch.data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class WordInfo {

    @Pattern(regexp = "^[a-zA-Z]+$",
            message = "{word.error.empty}")
    private String name;

    @NotEmpty(message = "{word.category.error.message}")
    private String category;

    @NotEmpty(message = "{word.definition.error.empty}")
    private String definition;

    private String example;

    @NotEmpty(message = "{word.source.error.message}")
    private String source;

    private String url;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WordInfo{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", definition='" + definition + '\'' +
                ", example='" + example + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

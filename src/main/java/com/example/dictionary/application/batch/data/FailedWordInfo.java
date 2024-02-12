package com.example.dictionary.application.batch.data;

public class FailedWordInfo {

    private String name;

    private String category;

    private String definition;

    private String example;

    private String source;

    private String url;

    private String error;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "FailedWordInfo{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", definition='" + definition + '\'' +
                ", example='" + example + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}

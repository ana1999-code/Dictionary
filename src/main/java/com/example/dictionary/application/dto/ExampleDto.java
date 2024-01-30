package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public class ExampleDto {

    private Integer id;

    @NotEmpty(message = "{word.example.error.empty}")
    private String text;

    public ExampleDto() {
    }

    public ExampleDto(String text) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleDto that = (ExampleDto) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}

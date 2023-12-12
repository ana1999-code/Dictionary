package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotBlank;

public class ExampleDto {

    private Integer id;

    @NotBlank
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
}

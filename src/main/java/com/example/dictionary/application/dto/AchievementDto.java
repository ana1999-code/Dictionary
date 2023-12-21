package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AchievementDto {

    private Integer id;

    @NotEmpty
    private String name;

    @NotNull
    private Integer numberOfWordsRequired;

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

    public Integer getNumberOfWordsRequired() {
        return numberOfWordsRequired;
    }

    public void setNumberOfWordsRequired(Integer numberOfWordsRequired) {
        this.numberOfWordsRequired = numberOfWordsRequired;
    }
}

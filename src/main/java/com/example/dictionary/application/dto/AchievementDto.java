package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class AchievementDto {

    private Integer id;

    @NotEmpty
    private String name;

    @NotNull
    private Integer numberOfWordsRequired;

    public AchievementDto(String name, Integer numberOfWordsRequired) {
        this.name = name;
        this.numberOfWordsRequired = numberOfWordsRequired;
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

    public Integer getNumberOfWordsRequired() {
        return numberOfWordsRequired;
    }

    public void setNumberOfWordsRequired(Integer numberOfWordsRequired) {
        this.numberOfWordsRequired = numberOfWordsRequired;
    }

    @Override
    public String toString() {
        return "AchievementDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfWordsRequired=" + numberOfWordsRequired +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchievementDto that = (AchievementDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(numberOfWordsRequired, that.numberOfWordsRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfWordsRequired);
    }
}

package com.example.dictionary.application.report.data;

import com.example.dictionary.domain.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class WordDetail {

    private Integer id;

    private String name;

    private String category;

    private User contributor;

    private LocalDateTime addedAt;

    public WordDetail(Integer id, String name, String category, User contributor, LocalDateTime addedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.contributor = contributor;
        this.addedAt = addedAt;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getContributor() {
        return contributor;
    }

    public void setContributor(User contributor) {
        this.contributor = contributor;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordDetail that = (WordDetail) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(category, that.category)
                && Objects.equals(contributor, that.contributor)
                && Objects.equals(addedAt, that.addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, contributor, addedAt);
    }
}

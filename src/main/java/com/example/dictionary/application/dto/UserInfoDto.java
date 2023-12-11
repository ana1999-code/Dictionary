package com.example.dictionary.application.dto;

import java.util.Set;

public class UserInfoDto {

    private Integer id;

    private Integer level;

    private Integer progress;

    private Set<AchievementDto> achievements;

    private Set<WordDto> favorites;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Set<AchievementDto> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<AchievementDto> achievements) {
        this.achievements = achievements;
    }

    public Set<WordDto> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<WordDto> favorites) {
        this.favorites = favorites;
    }
}

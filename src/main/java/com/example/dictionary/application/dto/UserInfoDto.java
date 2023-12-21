package com.example.dictionary.application.dto;

import java.util.HashSet;
import java.util.Set;

public class UserInfoDto {

    private Integer id;

    private Integer progress;

    private Set<AchievementDto> achievements = new HashSet<>();

    private Set<WordDto> favorites = new HashSet<>();

    public UserInfoDto() {
    }

    public UserInfoDto(Integer progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

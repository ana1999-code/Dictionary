package com.example.dictionary.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private Integer level;

    private Integer progress;

    @OneToMany(mappedBy = "id.userInfo")
    private Set<UserAchievement> achievement;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id"))
    private Set<Word> favorites;

    public UserInfo() {
    }

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

    public Set<UserAchievement> getAchievement() {
        return achievement;
    }

    public void setAchievement(Set<UserAchievement> achievement) {
        this.achievement = achievement;
    }

    public Set<Word> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Word> favorites) {
        this.favorites = favorites;
    }
}

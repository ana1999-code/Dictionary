package com.example.dictionary.domain.entity;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDate;

@Entity
@Table(name = "user_achievement")
@AssociationOverrides({
        @AssociationOverride(name = "id.userInfo",
                joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "id.achievement",
                joinColumns = @JoinColumn(name = "achievement_id"))
})
public class UserAchievement {

    @EmbeddedId
    private UserAchievementId id = new UserAchievementId();

    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate achievedAt;

    public UserAchievement() {
    }

    public UserAchievementId getId() {
        return id;
    }

    public void setId(UserAchievementId id) {
        this.id = id;
    }

    public LocalDate getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(LocalDate achievedAt) {
        this.achievedAt = achievedAt;
    }
}

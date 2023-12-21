package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
}

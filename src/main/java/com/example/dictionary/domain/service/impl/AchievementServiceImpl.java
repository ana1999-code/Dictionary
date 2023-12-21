package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Achievement;
import com.example.dictionary.domain.repository.AchievementRepository;
import com.example.dictionary.domain.service.AchievementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;

    public AchievementServiceImpl(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    @Override
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
}

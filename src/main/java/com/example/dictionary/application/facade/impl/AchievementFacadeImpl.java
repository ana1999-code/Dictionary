package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.application.facade.AchievementFacade;
import com.example.dictionary.application.mapper.AchievementMapper;
import com.example.dictionary.domain.entity.Achievement;
import com.example.dictionary.domain.service.AchievementService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AchievementFacadeImpl implements AchievementFacade {

    private final AchievementService achievementService;

    private final AchievementMapper achievementMapper;

    public AchievementFacadeImpl(AchievementService achievementService,
                                 AchievementMapper achievementMapper) {
        this.achievementService = achievementService;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public List<AchievementDto> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        return achievements.stream()
                .map(achievementMapper::achievementToAchievementDto)
                .toList();
    }
}

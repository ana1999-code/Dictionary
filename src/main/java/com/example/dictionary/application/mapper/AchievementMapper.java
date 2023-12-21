package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.domain.entity.Achievement;
import org.mapstruct.Mapper;

@Mapper
public interface AchievementMapper {

    Achievement achievementDtoToAchievement(AchievementDto achievementDto);

    AchievementDto achievementToAchievementDto(Achievement achievement);
}

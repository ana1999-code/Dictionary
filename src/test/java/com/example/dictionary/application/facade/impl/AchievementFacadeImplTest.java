package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.application.mapper.AchievementMapper;
import com.example.dictionary.domain.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.dictionary.utils.TestUtils.ACHIEVEMENT;
import static com.example.dictionary.utils.TestUtils.ACHIEVEMENT_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementFacadeImplTest {

    @InjectMocks
    private AchievementFacadeImpl achievementFacade;

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementMapper achievementMapper;

    @Test
    void testGetAllAchievements() {
        when(achievementService.getAllAchievements()).thenReturn(List.of(ACHIEVEMENT));
        when(achievementMapper.achievementToAchievementDto(any())).thenReturn(ACHIEVEMENT_DTO);

        List<AchievementDto> actualResult = achievementFacade.getAllAchievements();

        assertTrue(actualResult.contains(ACHIEVEMENT_DTO));
        verify(achievementService).getAllAchievements();
        verify(achievementMapper).achievementToAchievementDto(any());
    }
}
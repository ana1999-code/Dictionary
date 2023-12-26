package com.example.dictionary.application.aop;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.UserInfoDto;
import com.example.dictionary.application.facade.AchievementFacade;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.application.mapper.UserMapper;
import com.example.dictionary.application.security.utils.SecurityUtils;
import com.example.dictionary.domain.entity.Word;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@Aspect
public class WordContributionAspect {

    private final UserFacade userFacade;

    private final UserMapper userMapper;

    private final WordFacade wordFacade;

    private final AchievementFacade achievementFacade;

    public WordContributionAspect(UserFacade userFacade,
                                  UserMapper userMapper, WordFacade wordFacade,
                                  AchievementFacade achievementFacade) {
        this.userFacade = userFacade;
        this.userMapper = userMapper;
        this.wordFacade = wordFacade;
        this.achievementFacade = achievementFacade;
    }

    @Pointcut("@annotation(com.example.dictionary.application.annotation.ContributeByUser)")
    public void contributeByUserAnnotation() {
    }


    @Before("contributeByUserAnnotation()")
    public void setWordContributor(JoinPoint joinPoint) {
        UserDto user = getUser();

        Object arg = Arrays.stream(joinPoint.getArgs()).toList().get(0);

        if (arg instanceof Word word) {
            word.getContributors().add(userMapper.userDtoToUser(user));
        }
    }

    @AfterReturning("contributeByUserAnnotation()")
    public void setUserProgress() {
        UserDto user = getUser();

        int progress = user.getUserInfo().getProgress();
        user.getUserInfo().setProgress(progress + 1);

        userFacade.updateUserProgress(user);
    }

    @AfterReturning("execution(* com.example.dictionary.application.facade.UserFacade.updateUserProgress(..))")
    public void setUserAchievements() {
        UserDto user = getUser();

        UserInfoDto userInfo = user.getUserInfo();
        Integer progress = userInfo.getProgress();
        Set<AchievementDto> userAchievements = userInfo.getAchievements();
        List<AchievementDto> allAchievements = achievementFacade.getAllAchievements();

        allAchievements.forEach(achievement -> {
            if (achievement.getNumberOfWordsRequired().equals(progress)
                    && !userAchievements.contains(achievement)) {
                userFacade.addAchievement(achievement);
            }
        });
    }

    private UserDto getUser() {
        String loggedInUser = SecurityUtils.getUsername();
        return userFacade.findUserByEmail(loggedInUser);
    }
}

package com.example.dictionary.application.aop;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.facade.WordFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class WordContributionAspect {

    private final UserFacade userFacade;

    private final WordFacade wordFacade;

    public WordContributionAspect(UserFacade userFacade,
                                  WordFacade wordFacade) {
        this.userFacade = userFacade;
        this.wordFacade = wordFacade;
    }

    @Pointcut("@annotation(com.example.dictionary.application.annotation.ContributeByUser)")
    public void contributeByUserAnnotation() {
    }


    @Before("contributeByUserAnnotation()")
    public void setWordContributor(JoinPoint joinPoint) {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = userFacade.findUserByEmail(loggedInUser);

        Object arg = Arrays.stream(joinPoint.getArgs()).toList().get(0);

        if (arg instanceof WordDto wordDto) {
            wordDto.getContributors().add(user);
        } else {
            String wordName = (String) Arrays.stream(joinPoint.getArgs()).toList().get(0);
            wordFacade.getWordByName(wordName).getContributors().add(user);
        }
    }

    @AfterReturning("contributeByUserAnnotation()")
    public void setUserProgress() {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = userFacade.findUserByEmail(loggedInUser);

        Integer progress = user.getUserInfo().getProgress();
        user.getUserInfo().setProgress(progress + 1);

        userFacade.updateUserProgress(user);
    }
}

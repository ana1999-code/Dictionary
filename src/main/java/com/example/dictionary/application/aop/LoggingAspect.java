package com.example.dictionary.application.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.dictionary.application.facade..get*(..))")
    public void getEntity() {
    }

    @Pointcut("execution(* com.example.dictionary.application.facade..add*(..))")
    public void addEntity() {
    }

    @Pointcut("execution(* com.example.dictionary.application.facade..delete*(..))")
    public void deleteEntity() {
    }

    @Pointcut("execution(* com.example.dictionary.application.facade..remove*(..))")
    public void removeEntity() {
    }

    @Pointcut("execution(* com.example.dictionary.application.facade.AchievementFacade.*(..))")
    public void getAchievements() {
    }

    @Pointcut("execution(* com.example.dictionary.application.facade.UserFacade.register*(..))")
    public void registerUser(){}

    @Pointcut("execution(* com.example.dictionary.application.facade.UserFacade.upload*(..))")
    public void uploadImage(){}

    @Around("(getEntity() || addEntity() || deleteEntity() || removeEntity() || registerUser() || uploadImage()) " +
            "&& !getAchievements() ")
    public Object getEntities(ProceedingJoinPoint joinPoint) {
        Object proceed;
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.stream(joinPoint.getArgs()).toList();
        LOGGER.info("{}: {} with arguments {}", className, methodName, args);
        try {
            proceed = joinPoint.proceed();
            LOGGER.info("{}: {} executed successfully", className, methodName);
            if (proceed != null) {
                LOGGER.info("{}: Returned response after {}: {}", className, methodName, proceed);
            }
        } catch (Throwable e) {
            LOGGER.error("{}: error when {} with arguments {} -> {}", className, methodName, args, e.getMessage());
            throw new RuntimeException(e);
        }
        return proceed;
    }
}

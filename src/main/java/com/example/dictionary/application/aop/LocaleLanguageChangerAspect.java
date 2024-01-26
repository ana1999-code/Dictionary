package com.example.dictionary.application.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;

@Component
@Aspect
public class LocaleLanguageChangerAspect {

    @Around("execution(* com.example.dictionary.rest.controller.*Controller.*(..))")
    public Object changeLocaleLanguageOnHttpRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        String header = request.getHeader("Accept-Language");
        if (header != null){
            Locale locale = Locale.forLanguageTag(header);
            Locale.setDefault(locale);
        }

        return joinPoint.proceed();
    }
}

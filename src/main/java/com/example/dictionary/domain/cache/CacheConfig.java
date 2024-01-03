package com.example.dictionary.domain.cache;

import com.example.dictionary.application.batch.listener.WordProcessorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableCaching
@EnableScheduling
@ConditionalOnProperty(value = "cache.enabled", havingValue = "true")
public class CacheConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(WordProcessorListener.class);

    @Bean("cacheKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CacheKeyGenerator();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                CacheContext.WORDS_CACHE,
                CacheContext.WORD_CACHE,
                CacheContext.WORDS_DETAILS_CACHE,
                CacheContext.USERS_CACHE,
                CacheContext.USER_CACHE
        );
    }

    @CacheEvict(allEntries = true, value = {
            CacheContext.WORDS_CACHE,
            CacheContext.WORD_CACHE,
            CacheContext.WORDS_DETAILS_CACHE,
            CacheContext.USERS_CACHE,
            CacheContext.USER_CACHE
    })
    @Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 500)
    public void reportCacheEvict() {
        LOGGER.info("Flush Cache " +
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS").format(new Date()));
    }
}

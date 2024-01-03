package com.example.dictionary.domain.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + "_"
                + method.getName();
    }
}

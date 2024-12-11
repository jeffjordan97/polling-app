package com.dizplai.polling.service.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManagerForActivePoll(){
        return new ConcurrentMapCacheManager("activePoll");
    }
}

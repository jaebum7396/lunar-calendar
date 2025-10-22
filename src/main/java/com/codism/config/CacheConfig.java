package com.codism.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 캐시 설정
 * 사주 계산 결과를 메모리에 캐싱하여 DB 조회 최소화
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "sipsung",          // 천간십성 계산 결과 캐시
                "jijiSipsung",      // 지지십성 계산 결과 캐시
                "sipsungDetail",    // 십성 상세 정보 캐시
                "sinsal",           // 신살 계산 결과 캐시
                "cheongan",         // 천간 마스터 캐시
                "jiji"              // 지지 마스터 캐시
        );
    }
}

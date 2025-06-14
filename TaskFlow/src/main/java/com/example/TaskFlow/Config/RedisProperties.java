package com.example.TaskFlow.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
        long tasksTtl,
        long projectsTtl,
        long usersTtl,
        String patternTopic
) {
}


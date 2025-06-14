package com.example.TaskFlow.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Long accessExpirationMs,
        Long refreshExpirationMs
) {
}

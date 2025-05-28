package com.example.TaskFlow.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "file")
public record FileStorageProperties(
        Long maxSize,
        Set<String> allowedTypes
) {
}

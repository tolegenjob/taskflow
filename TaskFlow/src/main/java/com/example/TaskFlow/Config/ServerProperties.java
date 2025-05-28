package com.example.TaskFlow.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public record ServerProperties(
        String port
) {}

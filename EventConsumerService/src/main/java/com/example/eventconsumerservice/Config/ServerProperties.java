package com.example.eventconsumerservice.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public record ServerProperties(
        String port
) {}

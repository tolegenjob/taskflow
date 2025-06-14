package com.example.TaskFlow.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer(
            @Value("${spring.data.web.pageable.default-page-number}") int defaultPage,
            @Value("${spring.data.web.pageable.default-page-size}") int defaultSize,
            @Value("${spring.data.web.pageable.max-page-size}") int maxPageSize
    ) {
        return resolver -> {
            resolver.setFallbackPageable(
                    PageRequest.of(defaultPage,
                            defaultSize)
            );
            resolver.setMaxPageSize(maxPageSize);
        };
    }
}


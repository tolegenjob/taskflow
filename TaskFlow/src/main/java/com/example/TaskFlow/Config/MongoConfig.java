package com.example.TaskFlow.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    public GridFsTemplate gridFsTemplate(MongoDatabaseFactory factory, MappingMongoConverter converter) {
        return new GridFsTemplate(factory, converter, "fs");
    }
}

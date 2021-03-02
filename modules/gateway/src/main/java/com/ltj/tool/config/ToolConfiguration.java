package com.ltj.tool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @describe： TODO
 * @Date: 2021-02-26 10:19
 * @author: admin
 */

@Slf4j
@EnableAsync
@Configuration(proxyBeanMethods = false)
public class ToolConfiguration {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.failOnEmptyBeans(false);
        return builder.build();
    }


}

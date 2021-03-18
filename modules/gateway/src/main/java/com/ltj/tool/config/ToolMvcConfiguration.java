package com.ltj.tool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Tool mvc configuration
 *
 * @author Liu Tian Jun
 * @date 2021-03-16 11:13
 */
@Slf4j
@Configuration
//@EnableConfigurationProperties(MultipartProperties.class)
//@ImportAutoConfiguration(exclude = MultipartAutoConfiguration.class)
public class ToolMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 支持请求参数协商
        configurer.favorParameter(true);
    }

}

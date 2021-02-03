package com.ltj.tool.config;

import com.ltj.tool.activiti.factory.ActResponseFactory;
import com.ltj.tool.activiti.resolver.ContentTypeResolver;
import com.ltj.tool.activiti.resolver.DefaultContentTypeResolver;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {
    /*@Bean
    public ProcessEngine processEngine(PlatformTransactionManager transactionManager, DataSource dataSource) throws IOException {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

        configuration.setActivityFontName("宋体");
        configuration.setAnnotationFontName("宋体");
        configuration.setLabelFontName("宋体");

        return configuration.buildProcessEngine();
    }*/

    /**
     * 解決工作流生成图片乱码问题
     *
     * @param processEngineConfiguration processEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
    }

    @Bean()
    public ActResponseFactory restResponseFactory() {
        ActResponseFactory restResponseFactory = new ActResponseFactory();
        return restResponseFactory;
    }

    @Bean()
    public ContentTypeResolver contentTypeResolver() {
        ContentTypeResolver resolver = new DefaultContentTypeResolver();
        return resolver;
    }
}

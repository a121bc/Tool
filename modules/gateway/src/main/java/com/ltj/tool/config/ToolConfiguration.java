package com.ltj.tool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltj.tool.cache.AbstractStringCacheStore;
import com.ltj.tool.cache.InMemoryCacheStore;
import com.ltj.tool.cache.LevelCacheStore;
import com.ltj.tool.config.properties.KaoQinTongProperties;
import com.ltj.tool.config.properties.ToolProperties;
import com.ltj.tool.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @describe： Tool configuration
 * @Date: 2021-02-26 10:19
 * @author: admin
 */

@Slf4j
@EnableAsync
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ToolProperties.class, KaoQinTongProperties.class})
public class ToolConfiguration {

    private ToolProperties toolProperties;
    private KaoQinTongProperties kaoQinTongProperties;

    public ToolConfiguration(ToolProperties toolProperties, KaoQinTongProperties kaoQinTongProperties) {
        this.toolProperties = toolProperties;
        this.kaoQinTongProperties = kaoQinTongProperties;
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.failOnEmptyBeans(false);
        return builder.build();
    }

    @Bean
    public RestTemplate httpsRestTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplate httpsRestTemplate = builder.build();
        httpsRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(
                HttpClientUtils.createHttpsClient((int) toolProperties.getDownloadTimeout().toMillis())
        ));
        return httpsRestTemplate;

    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractStringCacheStore stringCacheStore() {
        AbstractStringCacheStore stringCacheStore;
        switch (toolProperties.getCache()) {
            case "level":
                stringCacheStore = new LevelCacheStore(this.toolProperties);
                break;
            case "memory":
            default:
                // memory or default
                stringCacheStore = new InMemoryCacheStore();
                break;
        }
        log.info("Tool cache store load impl : [{}]", stringCacheStore.getClass());
        return stringCacheStore;

    }

}

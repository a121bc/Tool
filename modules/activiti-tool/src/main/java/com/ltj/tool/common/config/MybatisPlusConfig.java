package com.ltj.tool.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @describe： MybatisPlus 配置
 * @author: Liu Tian Jun
 * @Date: 2019-12-12 15:55
 * @version: 1.0
 *
 * 字段映射错误时，请注意关闭自动驼峰转换
 *
 * mybatis-plus.configuration.map-underscore-to-camel-case=false
 *
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.ltj.tool.module.*.mapper")
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}

package com.gov.gov_work.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有接口路径都支持跨域
        registry.addMapping("/**")
                // 允许所有来源的前端地址
                .allowedOriginPatterns("*")
                // 允许的请求方式
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许携带请求头
                .allowedHeaders("*")
                // 允许携带cookie/token
                .allowCredentials(true)
                // 预检请求有效期，3600秒内不用重复发预检
                .maxAge(3600);
    }
}

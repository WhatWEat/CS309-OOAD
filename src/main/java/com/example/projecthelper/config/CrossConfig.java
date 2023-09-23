package com.example.projecthelper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:9090")
            .allowedMethods("*")
            .allowedOriginPatterns("*").allowCredentials(true);
        registry.addMapping("/static/**")
            .allowedOrigins("http://localhost:9090")
            .allowedMethods("*")
            .allowedOriginPatterns("*").allowCredentials(true);
    }
}

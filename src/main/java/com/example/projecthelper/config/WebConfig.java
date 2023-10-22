package com.example.projecthelper.config;

import java.lang.reflect.Field;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:9000")
            .allowedMethods("*")
            .allowedOriginPatterns("*").allowCredentials(true);
        registry.addMapping("/static/**")
            .allowedOrigins("http://localhost:9000")
            .allowedMethods("*")
            .allowedOriginPatterns("*").allowCredentials(true);
    }


}

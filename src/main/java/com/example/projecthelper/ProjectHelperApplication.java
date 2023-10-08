package com.example.projecthelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.example.projecthelper.mapper")
public class ProjectHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectHelperApplication.class, args);
    }

}

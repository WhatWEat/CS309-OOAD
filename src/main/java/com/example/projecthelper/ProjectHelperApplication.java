package com.example.projecthelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ProjectHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectHelperApplication.class, args);
    }

}

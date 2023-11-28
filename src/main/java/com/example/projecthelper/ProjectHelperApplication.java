package com.example.projecthelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
@MapperScan("com.example.projecthelper.mapper")
public class ProjectHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectHelperApplication.class, args);
    }



}

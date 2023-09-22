package com.example.projecthelper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tea")
public class TeacherController {
    @GetMapping("/test")
    public String get(){
        return "hello231313";
    }
}

package com.example.projecthelper.controller;

import com.example.projecthelper.service.LoginService;
import com.example.projecthelper.util.security.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    private LoginService loginService;
    private final static Logger log = LoggerFactory.getLogger(SecurityController.class);


    @GetMapping("/test")
    // 会自动跳转到/login
    public String get(){
        return "hello231313";
    }

    @GetMapping("/login")
    public String login_test(){
        log.info("test, log successful");
        return "hello";
    }

    @PostMapping("/login")
    //TODO:@RequestBody 为user类型
    public ResponseResult login(){
        return null;
    }


}

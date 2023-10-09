package com.example.projecthelper.controller;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    private final AuthService authService;
    private final static Logger log = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    public SecurityController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping("/test")
    // 会自动跳转到/login
    public String get(){
        return "hello231313";
    }

    @GetMapping("/login")
    public ResponseResult<Object> login_test(){
        return ResponseResult.ok(null, "原神，启动", authService.checkLoginAndIdentity());
    }

    @GetMapping("/signup")
    public String signup_test(){
        log.info("test, log successful");
        return "signup";
    }

    @PostMapping("/signup")
    public ResponseResult<Long> signup_test2(@RequestBody User user){
        log.info("test, log successful");
        return ResponseResult.ok(authService.registerUser(user), "", "");
    }

    @PostMapping("/login")
    //TODO:@RequestBody 为user类型
    public ResponseResult<Object> login(@RequestBody User user){
        return ResponseResult.ok("hello", "hi", "");
    }

    @GetMapping("/id")
    public ResponseResult<String> getId(){
        String jwt = authService.checkLoginAndIdentity();

        return ResponseResult.ok(JWTUtil.getUserIdByToken(jwt), "success", JWTUtil.updateJWT(jwt));
    }


    /** 数据库功能测试
     *
     */


}

package com.example.projecthelper.controller;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.util.FormatUtil;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.IdentityCode;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/t1")
    // 会自动跳转到/login
    public String post(){
        return "hello231313";
    }


    @GetMapping("/login")
    public ResponseResult<Object> login_test(HttpServletRequest request){
        System.out.println(request);
        return ResponseResult.ok(null, "原神，启动", JWTUtil.createJWT("1", "启动"));
    }

    @GetMapping("/signup")
    public String signup_test(){
        log.info("test, log successful");
        return "signup";
    }


    //TODO:
    @PostMapping("/signup")
    public ResponseResult<Long> signup(@RequestBody User user){
        String jwt = authService.registerUser(user);
        return ResponseResult.ok(null, "", jwt);
    }

    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody KeyValueWrapper userPass){
        String jwt = authService.login(userPass);
        //NOTE: 校验失败后，spring-security自动跑出并捕获AuthenticationException异常
        return ResponseResult.ok(null, "success", jwt);
    }

    //登出：清理token
    @DeleteMapping("/logout")
    public ResponseResult<Object> logout(){
        return ResponseResult.ok(null, "登出成功", authService.logout());
    }




    @GetMapping("/id")
    public ResponseResult<String> getId(HttpServletRequest request){
        String jwt = null;

        return ResponseResult.ok(null, "success", null);

    }


    /** 数据库功能测试
     *
     */


}

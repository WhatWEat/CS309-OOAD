package com.example.projecthelper.controller;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.ObjectCountWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm")
public class AdministratorController {

    @Autowired
    AuthService authService;

    @GetMapping("/test")
    public ResponseResult<Object> test(){
        return ResponseResult.ok(null, "Success", null);
    }

    @PostMapping("/createMultipleUsers")
    public ResponseResult<Object> createMultipleUsers(@RequestBody ObjectCountWrapper<User> multiUsers, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        authService.registerUser(multiUsers);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }


    /** 数据库功能测试
     *
     */

}

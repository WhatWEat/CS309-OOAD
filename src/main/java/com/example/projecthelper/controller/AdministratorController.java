package com.example.projecthelper.controller;

import com.example.projecthelper.util.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm")
public class AdministratorController {

    @GetMapping("/test")
    public ResponseResult<Object> test(){
        return ResponseResult.ok(null, "Success", null);
    }



    /** 数据库功能测试
     *
     */

}

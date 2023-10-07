package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.service.GroupService;
import com.example.projecthelper.service.LoginService;
import com.example.projecthelper.service.UserService;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stu")
public class StudentController {
    private final LoginService loginService;
    private final UserService userService;
    private final GroupService groupService;
    @Autowired
    public StudentController(LoginService loginService, UserService userService,
                             GroupService groupService) {
        this.loginService = loginService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @PutMapping("/editPersonInfo")
    public ResponseResult<Object> editPersonInfo(User user){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        if(!userService.editPersonInfo(user, jwt))
            return ResponseResult.unAuthorize(null, "unable to edit others information");
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/editPersonInfo")
    public ResponseResult<Object> joinGroup(Group group){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        groupService.joinGroup(group, jwt);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
}

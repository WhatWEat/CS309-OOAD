package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.service.GroupService;
import com.example.projecthelper.service.LoginService;
import com.example.projecthelper.service.NoticeService;
import com.example.projecthelper.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tea")
public class TeacherController {
    private final LoginService loginService;
    private final NoticeService noticeService;
    private final GroupService groupService;


    @Autowired
    public TeacherController(LoginService loginService, NoticeService noticeService,
                             GroupService groupService) {
        this.loginService = loginService;
        this.noticeService = noticeService;
        this.groupService = groupService;
    }

    @GetMapping("/test")
    public String get(){
        return "hello231313";
    }

    @PostMapping("/postNotice")
    public ResponseResult<Object> postNotice(@RequestBody Notice notice){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        noticeService.postNotice(notice);
        return ResponseResult.ok(null, "Success", jwt);
    }

    @PutMapping("/modifyNotice")
    public ResponseResult<Object> modifyNotice(@RequestBody Notice notice){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        if(!noticeService.modifyNoticeWithUser(notice, jwt))
            return ResponseResult.unAuthorize(null, "unable to modify notices posted by other");
        return ResponseResult.ok(null, "Success", jwt);
    }

    @PostMapping("/createGroup")
    public ResponseResult<Object> createGroup(Group group){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        groupService.createGroup(group);
        return ResponseResult.ok(null, "Success", jwt);
    }

    @PutMapping("/modifyGroupInfo")
    public ResponseResult<Object> modifyGroupInfo(Group group){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        groupService.modifyGroupInfo(group);
        return ResponseResult.ok(null, "Success", jwt);
    }

}

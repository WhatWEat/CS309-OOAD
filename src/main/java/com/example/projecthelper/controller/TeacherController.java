package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.util.JWTUtil;

import com.example.projecthelper.service.*;
import com.example.projecthelper.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@RestController
@RequestMapping("/tea")
public class TeacherController {
    private final LoginService loginService;
    private final NoticeService noticeService;
    private final GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

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
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/modifyNotice")
    public ResponseResult<Object> modifyNotice(@RequestBody Notice notice){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        if(!noticeService.modifyNoticeWithUser(notice, jwt))
            return ResponseResult.unAuthorize(null, "unable to modify notices posted by other");
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/createGroup")
    public ResponseResult<Object> createGroup(Group group){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        groupService.createGroup(group);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/modifyGroupInfo")
    public ResponseResult<Object> modifyGroupInfo(Group group){
        String jwt = loginService.checkLoginAndIdentity();
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        groupService.modifyGroupInfo(group);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

  
  
  
    // 数据库功能测试
    @PostMapping("/registerTea/{password}/{name}/{gender}")
    //注册教师,返回教师的user_id
    public long registerTea(@PathVariable String password,
                            @PathVariable String name,
                            @PathVariable String gender) {
        return userService.registerUser(1, password, name, gender);
    }


    @PostMapping("/createProject/{name}")
    //创建新的project
    public void createProject(@PathVariable String name) {
        projectService.createProject(name);
    }

    @PostMapping("/createNotice/{title}/{content}/{creator_id}")
    //创建新的notice
    public void createNotice(@PathVariable String title,
                             @PathVariable String content,
                             @PathVariable Integer creator_id) {
        noticeService.createNotice(title, content, creator_id);
    }

    @PostMapping("/createPluralGroup/{instructor_id}/{max_size}/{project_id}/{team_time}/{deadline}/{number}")
    //创建复数个新的小组,其中number代表创建小组的数量，team_time表示组队截止时间，instructor_id表示指导老师的id
    public long[] createPluralGroup(@PathVariable long instructor_id,
                                    @PathVariable long max_size,
                                    @PathVariable long project_id,
                                    @PathVariable Timestamp team_time,
                                    @PathVariable Timestamp deadline,
                                    @PathVariable int number) {
        return groupService.createPluralGroup(instructor_id, max_size, project_id, team_time, deadline, number);
    }

    @PostMapping("/createOneGroup/{instructor_id}/{max_size}/{project_id}/{team_time}/{deadline}/{group_name}")
    //创建单个小组，team_time表示组队截止时间，instructor_id表示指导老师的id
    public long createOneGroup(@PathVariable long instructor_id,
                               @PathVariable long max_size,
                               @PathVariable long project_id,
                               @PathVariable Timestamp team_time,
                               @PathVariable Timestamp deadline,
                               @PathVariable String group_name) {
        return groupService.createOneGroup(instructor_id, max_size, project_id, team_time, deadline, group_name);
    }

    @PostMapping("/updateGroupSize/{max_size}/{group_id}")
    //修改某一小组的最大人数
    public void updateGroupSize(@PathVariable long max_size,
                                @PathVariable long group_id) {
        groupService.updateGroupSize(max_size, group_id);
    }

    @PostMapping("/updateGroupInstructor/{instructor_id}/{group_id}")
    //修改某一小组的指导老师
    public void updateGroupInstructor(@PathVariable long instructor_id,
                                      @PathVariable long group_id) {
        groupService.updateGroupInstructor(instructor_id, group_id);
    }



    @PostMapping("/updateGroupTime/{team_time}/{group_id}")
    //修改某一小组的组队截止时间
    public void updateGroupTime(@PathVariable Timestamp team_time,
                                @PathVariable long group_id) {
        groupService.updateGroupTime(team_time, group_id);
    }

    @PostMapping("/updateGroupDeadline/{deadline}/{group_id}")
    //修改某一小组的答辩时间
    public void updateGroupDeadline(@PathVariable Timestamp deadline,
                                    @PathVariable long group_id) {
        groupService.updateGroupDeadline(deadline, group_id);
    }

}

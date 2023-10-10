package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.service.GroupService;
import com.example.projecthelper.service.UserService;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stu")
public class StudentController {
    private final AuthService authService;
    private final UserService userService;
    private final GroupService groupService;
    @Autowired
    public StudentController(AuthService authService, UserService userService,
                             GroupService groupService) {
        this.authService = authService;
        this.userService = userService;
        this.groupService = groupService;
    }


    @GetMapping("/test")
    public ResponseResult<Object> test(){
        return ResponseResult.ok(null, "Success", null);
    }

    @PutMapping("/editPersonInfo")
    public ResponseResult<Object> editPersonInfo(HttpServletRequest request, User user){
        String jwt = null;
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        if(!userService.editPersonInfo(user, jwt))
            return ResponseResult.unAuthorize(null, "unable to edit others information");
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/editPersonInfo")
    public ResponseResult<Object> joinGroup(HttpServletRequest request, Group group){
        String jwt = null;
        if(jwt == null)
            return ResponseResult.unAuthorize(null, "authentication failed");
        groupService.joinGroup(group, jwt);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/hello123")
    public String hello(){
        return "hello";
    }



    /** 数据库功能测试
     *
     */
//    @PostMapping("/registerStu/{password}/{name}/{gender}")
//    //注册学生,返回学生的user_id
//    public long registerTea(@PathVariable String password,
//                            @PathVariable String name,
//                            @PathVariable String gender){
//        return userService.registerUser(0,password,name,gender);
//    }


    @PostMapping("/stuJoinGroup/{group_id}/{stu_id}/{project_id}")
    //学生加入小组,如在该project仍未加入小组则加入成功，反之则失败
    public String stuJoinGroup(@PathVariable long group_id,
                               @PathVariable long stu_id,
                               @PathVariable long project_id){
        if (groupService.findGroupOfStuInProject(stu_id,project_id)!=null) {
            groupService.stuJoinGroup(group_id, stu_id);
            return "join successful";
        }else {
            return "you have joined another group";
        }
    }

    @PostMapping("/updateGroupName/{group_name}/{group_id}")
    //修改某一小组的小组名
    public void updateGroupName(@PathVariable String group_name,
                                @PathVariable long group_id) {
        groupService.updateGroupName(group_name, group_id);
    }

    @PostMapping("/stuLeaveGroup/{group_id}/{stu_id}")
    //学生退出小组
    public void stuLeaveGroup(@PathVariable long group_id,
                              @PathVariable long stu_id){
        groupService.stuLeaveGroup(group_id, stu_id);
    }

    @GetMapping("/findGroupByProject/{stu_id}/{project_id}")
    //寻找特定project下学生已经加入的小组
    public Group findGroupByProject(@PathVariable long stu_id,
                                    @PathVariable long project_id){
        return groupService.findGroupOfStuInProject(stu_id,project_id);
    }

    @GetMapping("/findUndermannedGroup/{project_id}")
    //浏览特定project下人数未满的小组
    public List<Group> findUndermannedGroup(@PathVariable long project_id){
        return groupService.findUndermannedGroup(project_id);
    }

    @GetMapping("/findAllGroup/{project_id}")
    //浏览特定project下所有的小组
    public List<Group> findAllGroup(@PathVariable long project_id){
        return groupService.findAllGroup(project_id);
    }

    @GetMapping("/findMemberOfGroup/{group_id}")
    //获取小组当前人数
    public int findMemberOfGroup(@PathVariable long group_id){
        return groupService.findMemberOfGroup(group_id);
    }
}

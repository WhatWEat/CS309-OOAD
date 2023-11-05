package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;

import com.example.projecthelper.service.*;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import com.example.projecthelper.util.Wrappers.ObjectCountWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@RestController
@RequestMapping("/tea")
public class TeacherController {
    private final AuthService authService;
    private final NoticeService noticeService;
    private final GroupService groupService;
    private final AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    public TeacherController(AuthService authService, NoticeService noticeService,
                             GroupService groupService, AssignmentService assignmentService) {
        this.authService = authService;
        this.noticeService = noticeService;
        this.groupService = groupService;
        this.assignmentService = assignmentService;
    }



    @PostMapping("/createProject")
    public  ResponseResult<Object> createProject(@RequestBody Project proj, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        proj.setTeacherId(Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        projectService.createProject(proj);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/addStuToProject")
    public ResponseResult<Object> addStuToProject(HttpServletRequest request, @RequestBody
    KeyValueWrapper<Long, List<Long>> pjId_stuId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        projectService.addStuToProject(pjId_stuId.getKey(), pjId_stuId.getValue(), Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/post_notice")
    public ResponseResult<Object> postNotice(@RequestBody Notice notice, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        System.err.println(jwt);
        System.err.println(JWTUtil.getExpiredTime(jwt));
        noticeService.postNotice(
            notice,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            pjId -> Objects.equals(
                projectService.findTeacherByProject(pjId),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
            )
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/modify_notice")
    public ResponseResult<Object> modifyNotice(HttpServletRequest request, @RequestBody Notice notice){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        noticeService.modifyNoticeWithUser(
            notice,
            ntId -> Objects.equals(
                noticeService.findNoticeById(ntId).getCreatorId(),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
            )
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/delete_notice")
    public ResponseResult<Object> deleteNotice(HttpServletRequest request, @RequestBody Long noticeId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        noticeService.deleteNotice(
            noticeId,
            ntId -> Objects.equals(
                noticeService.findNoticeById(ntId).getCreatorId(),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
            )
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }


    @PostMapping("/createGroup")
    public ResponseResult<Object> createGroup(HttpServletRequest request, @RequestBody Group gp){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);

        groupService.createGroup(
            gp,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            pjId -> Objects.equals(
                projectService.findTeacherByProject(pjId),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
            )
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/createMultipleGroups")
    public ResponseResult<Object> createMultipleGroup(HttpServletRequest request, @RequestBody ObjectCountWrapper<Group> ocw){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);

        groupService.createGroup(
            ocw,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            pjId -> Objects.equals(
                projectService.findTeacherByProject(pjId),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
            )
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/modifyGroupInfo")
    public ResponseResult<Object> modifyGroupInfo(HttpServletRequest request, @RequestBody Group group){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.updateGroupForTea(
            group,
            gpId -> Objects.equals(
                groupService.findCreatorByGroup(gpId),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
            )
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/postAssignment")
    public ResponseResult<Object> postAssignment(HttpServletRequest request, @RequestBody Assignment assignment){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.createAss(
            assignment,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            pjId -> Objects.equals(
                projectService.findTeacherByProject(pjId),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
            )
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/viewAllSubmittedAss")
    public ResponseResult<List<SubmittedAssignment>> viewAllSubmittedAss(HttpServletRequest request, @RequestBody Long assignmentId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<SubmittedAssignment> submittedAssignments = assignmentService.viewAllSub(
            assignmentId,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(submittedAssignments, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/gradeAss")
    public ResponseResult<Object> gradeAss(HttpServletRequest request, @RequestBody SubmittedAssignment submittedAssignment){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.gradeAss(
            submittedAssignment,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }


//    @GetMapping("/getIfStuSub")
//    public ResponseResult<Object> getIfStuSub(HttpServletRequest request, @RequestBody KeyValueWrapper assignment){
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        boolean sub = assignmentService.ifStuSub()
//        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
//    }




    /** 数据库功能测试
     *
     */
//    @PostMapping("/registerTea/{password}/{name}/{gender}")
//    //注册教师,返回教师的user_id
//    public long registerTea(@PathVariable String password,
//                            @PathVariable String name,
//                            @PathVariable String gender) {
//        return userService.registerUser(1, password, name, gender);
//    }


//    @PostMapping("/createProject/{name}")
//    //创建新的project
//    public void createProject(@PathVariable String name) {
//        projectService.createProject(name);
//    }

//    @PostMapping("/createNotice/{title}/{content}/{creator_id}")
//    //创建新的notice
//    public void createNotice(@PathVariable String title,
//                             @PathVariable String content,
//                             @PathVariable Integer creator_id) {
//        noticeService.createNotice(title, content, creator_id);
//    }

//    @PostMapping("/createPluralGroup/{max_size}/{project_id}/{team_time}/{deadline}/{number}")
//    //创建复数个新的小组,其中number代表创建小组的数量，team_time表示组队截止时间，instructor_id表示指导老师的id
//    public long[] createPluralGroup(@PathVariable long max_size,
//                                    @PathVariable long project_id,
//                                    @PathVariable Timestamp team_time,
//                                    @PathVariable Timestamp deadline,
//                                    @PathVariable int number) {
//        return groupService.createPluralGroup( max_size, project_id, team_time, deadline, number);
//    }

//    @PostMapping("/createOneGroup/{max_size}/{project_id}/{team_time}/{deadline}/{group_name}")
//    //创建单个小组，team_time表示组队截止时间，instructor_id表示指导老师的id
//    public long createOneGroup(@PathVariable long max_size,
//                               @PathVariable long project_id,
//                               @PathVariable Timestamp team_time,
//                               @PathVariable Timestamp deadline,
//                               @PathVariable String group_name) {
//        return groupService.createOneGroup( max_size, project_id, team_time, deadline, group_name);
//    }

//    @PostMapping("/updateGroupSize/{max_size}/{group_id}")
//    //修改某一小组的最大人数
//    public void updateGroupSize(@PathVariable long max_size,
//                                @PathVariable long group_id) {
//        groupService.updateGroupSize(max_size, group_id);
//    }

//    @PostMapping("/updateGroupInstructor/{instructor_id}/{group_id}")
//    //修改某一小组的指导老师
//    public void updateGroupInstructor(@PathVariable long instructor_id,
//                                      @PathVariable long group_id) {
//        groupService.updateGroupInstructor(instructor_id, group_id);
//    }


//    @PostMapping("/updateGroupTime/{team_time}/{group_id}")
//    //修改某一小组的组队截止时间
//    public void updateGroupTime(@PathVariable Timestamp team_time,
//                                @PathVariable long group_id) {
//        groupService.updateGroupTime(team_time, group_id);
//    }

//    @PostMapping("/updateGroupDeadline/{deadline}/{group_id}")
//    //修改某一小组的答辩时间
//    public void updateGroupDeadline(@PathVariable Timestamp deadline,
//                                    @PathVariable long group_id) {
//        groupService.updateGroupDeadline(deadline, group_id);
//    }

}

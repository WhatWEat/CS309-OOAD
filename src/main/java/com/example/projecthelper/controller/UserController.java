package com.example.projecthelper.controller;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.service.AssignmentService;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.service.FileService;
import com.example.projecthelper.service.GroupService;
import com.example.projecthelper.service.NoticeService;
import com.example.projecthelper.service.ProjectService;
import com.example.projecthelper.service.UserService;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    private final FileService fileService;
    private final GroupService groupService;
    private final ProjectService projectService;

    private final AssignmentService assignmentService;

    private final NoticeService noticeService;
    private final static Logger log = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    public UserController(AuthService authService, UserService userService,
                          FileService fileService, GroupService groupService, ProjectService projectService,
                          AssignmentService assignmentService, NoticeService noticeService) {
        this.authService = authService;
        this.userService = userService;
        this.fileService = fileService;
        this.groupService = groupService;
        this.projectService = projectService;
        this.assignmentService = assignmentService;
        this.noticeService = noticeService;
    }

    @GetMapping("/get_pj_ntc_ass_cnt")
    public ResponseResult<List<Integer>> getCnt(
        HttpServletRequest request
    ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<Integer> cnt = userService.getCnt(Long.parseLong(JWTUtil.getUserIdByToken(jwt)), Long.parseLong(JWTUtil.getIdentityCodeByToken(jwt)));
        return ResponseResult.ok(cnt, "Success", JWTUtil.updateJWT(jwt));
    }
    @GetMapping("/project-list/{page}/{page_size}/{user_id}")
    public ResponseResult<List<Project>> getProjectList(
        HttpServletRequest request,
        @PathVariable int page,
        @PathVariable int page_size,
        @PathVariable Long user_id
    ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<Project> projects = projectService.getProjectList(
            user_id,
            page, page_size
        );
        return ResponseResult.ok(projects, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/intend_teammates/{project_id}/{user_id}")
    public ResponseResult<List<String>> getIntendTeammates(
        HttpServletRequest request,
        @PathVariable Long project_id,
        @PathVariable Long user_id
    ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        return ResponseResult.ok(projectService.getIntendedTeammates(project_id, user_id), "success", JWTUtil.updateJWT(jwt));
    }


    @GetMapping("/getGroupInfo/{group_id}")
    public ResponseResult<Group> getGroupById(
        HttpServletRequest request,
        @PathVariable("group_id") Long groupId
    ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Group group = groupService.getGroupById(groupId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(group, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/get_group_id/{proj_id}")
    public ResponseResult<Long> getGroupId(
        HttpServletRequest request,
        @PathVariable("proj_id") Long pjId
    ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Group group = groupService.findGroupOfStuInProject(Long.parseLong(JWTUtil.getUserIdByToken(jwt)), pjId);
        return ResponseResult.ok(group == null ? null: group.getGroupId(), "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/get_brief_groups_from_proj/{project_id}")
    public ResponseResult<List<Group>> getBriefGroupsFromProj(
        HttpServletRequest request,
        @PathVariable("project_id") Long projectId)
    {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<Group> groups = groupService.getBriefGroupsFromProj(projectId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(groups, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("ass/{ass_id}")
    public ResponseResult<KeyValueWrapper<Assignment, SubmittedAssignment>> getAssById(@PathVariable Long ass_id, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        int identity = Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt));
        KeyValueWrapper<Assignment, SubmittedAssignment> assignment = assignmentService.getAssById(ass_id, userId, identity);

        return ResponseResult.ok(assignment, "success", JWTUtil.updateJWT(jwt));
    }
    @GetMapping(value = "/ass-list/{project_id}/{page}/{page_size}")
    public ResponseResult<List<Assignment>> getAssignments(@PathVariable("project_id") Long projectId,
                                                           @PathVariable("page") long page,
                                                           @PathVariable("page_size") long pageSize,
                                                           HttpServletRequest request) {
        // Use the projectId, page, and pageSize in your method
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        List<Assignment> result = switch (Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))) {
            case 0 -> assignmentService.getAssignmentsByAdm(page, pageSize);
            case 1 -> assignmentService.getAssignmentsByTea(userId, projectId, page, pageSize);
            case 2 -> assignmentService.getAssignmentsByTa(userId, projectId, page, pageSize);
            case 3 -> assignmentService.getAssignmentsByStu(userId, projectId, page, pageSize);
            default -> throw new InvalidFormException("不合法的身份");
        };

        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping(value = {"/notice-list/{project_id}/{page}/{page_size}", "/notice-list/{project_id}/{page}/{page_size}/{search_key}"})
    public ResponseResult<List<Notice>> getNotices(@PathVariable("project_id") Long projectId,
                                                   @PathVariable("page") long page,
                                                   @PathVariable("page_size") long pageSize,
                                                   @PathVariable(value = "search_key", required = false) String searchKey,
                                                   HttpServletRequest request) {
        // Use the projectId, page, and pageSize in your method
        System.err.println(page+" "+pageSize+" "+searchKey+" "+projectId);
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        List<Notice> result = switch (Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))){
            case 0 -> noticeService.getNoticesByAdm(page, pageSize, searchKey);
            case 1 -> noticeService.getNoticesByTeacher(userId, projectId, page, pageSize, searchKey);
            case 2 -> noticeService.getNoticesByTa(userId, projectId, page, pageSize, searchKey);
            case 3 -> noticeService.getNoticesByStudent(userId, projectId, page, pageSize, searchKey);
            default -> throw new InvalidFormException("不合法的身份");
        };
        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
    }



}

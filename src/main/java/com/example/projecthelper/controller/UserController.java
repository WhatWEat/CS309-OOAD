package com.example.projecthelper.controller;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.service.AssignmentService;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.service.FileService;
import com.example.projecthelper.service.GroupService;
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
    private final static Logger log = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    public UserController(AuthService authService, UserService userService,
                          FileService fileService, GroupService groupService, ProjectService projectService,
                          AssignmentService assignmentService) {
        this.authService = authService;
        this.userService = userService;
        this.fileService = fileService;
        this.groupService = groupService;
        this.projectService = projectService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/project-list/{page}/{page_size}")
    public ResponseResult<List<Project>> getProjectList(
        HttpServletRequest request, @PathVariable int page, @PathVariable int page_size){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<Project> projects = projectService.getProjectList(
            new KeyValueWrapper<>(
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))
            ),
            page, page_size
        );
        return ResponseResult.ok(projects, "Success", JWTUtil.updateJWT(jwt));
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

    @GetMapping("/get_brief_groups_from_proj/{project_id}")
    public ResponseResult<List<Group>> getBriefGroupsFromProj(
        HttpServletRequest request,
        @PathVariable("project_id") Long projectId)
    {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<Group> groups = groupService.getBriefGroupsFromProj(projectId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(groups, "Success", JWTUtil.updateJWT(jwt));
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
            case 1 -> assignmentService.getAssignmentsByTea(userId, projectId, page, pageSize);
            case 2 -> assignmentService.getAssignmentsByTa(userId, projectId, page, pageSize);
            case 3 -> assignmentService.getAssignmentsByStu(userId, projectId, page, pageSize);
            default -> throw new InvalidFormException("不合法的身份");
        };

        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
    }


}

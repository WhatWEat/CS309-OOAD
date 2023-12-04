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
import com.example.projecthelper.util.FileUtil;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/get-list/{project_id}")
    public ResponseResult<List<Integer>> getCnt(
        HttpServletRequest request,
        @PathVariable Long project_id
    ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<Integer> cnt = userService.getCnt(
            project_id,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            Long.parseLong(JWTUtil.getIdentityCodeByToken(jwt))
        );
        return ResponseResult.ok(cnt, "Success", JWTUtil.updateJWT(jwt));
    }

    //PART I: project
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

    @GetMapping("/stu-list/{project_id}")
    public ResponseResult<KeyValueWrapper<List<Long>, List<String>>> stuList(
        HttpServletRequest request, @PathVariable("project_id") Long pjId
    ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        return ResponseResult.ok(
            projectService.getStuList(
                pjId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))
            ),
            "Success", JWTUtil.updateJWT(jwt)
        );
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

    @GetMapping(value = "/get_submitted_ass_file/{assignment_id}/{submitter_id}/{filename}")
    public ResponseEntity<Resource> getSubmittedAssFile(
        @PathVariable("assignment_id") Long assignmentId,
        @PathVariable("submitter_id") Long submitter_id,
        @PathVariable("filename") String filename,
        HttpServletRequest request) {

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Resource rec = fileService.getFilesOfSubmittedAss(userId, submitter_id, assignmentId, filename, Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt)));
        System.err.println(rec.getFilename());
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
            .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
            .body(rec);
    }

    @GetMapping(value = "/ass-list/{project_id}/{page}/{page_size}")
    public ResponseResult<List<Assignment>> getAssignments(@PathVariable("project_id") Long projectId,
                                                           @PathVariable("page") int page,
                                                           @PathVariable("page_size") int pageSize,
                                                           HttpServletRequest request) {
        // Use the projectId, page, and pageSize in your method
        System.err.println("here");
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

    @PostMapping("/post_notice")
    public ResponseResult<Object> postNotice(@RequestBody Notice notice, HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        System.err.println(notice);
        noticeService.postNotice(
            notice,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/modify_notice")
    public ResponseResult<Object> modifyNotice(HttpServletRequest request, @RequestBody Notice notice) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        noticeService.modifyNotice(
            notice,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/delete_notice")
    public ResponseResult<Object> deleteNotice(HttpServletRequest request, @RequestBody List<Long> noticeIds){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        noticeService.deleteNotice(
            noticeIds,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }



}

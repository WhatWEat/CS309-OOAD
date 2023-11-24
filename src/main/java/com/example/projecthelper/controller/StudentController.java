package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.service.AssignmentService;
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

import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/stu")
public class StudentController {
    private final UserService userService;
    private final NoticeService noticeService;
    private final GroupService groupService;
    private final AssignmentService assignmentService;
    private final FileService fileService;
    private final ProjectService projectService;

    @Autowired
    public StudentController(UserService userService,
                             NoticeService noticeService, GroupService groupService, AssignmentService assignmentService,
                             FileService fileService, ProjectService projectService) {
        this.userService = userService;
        this.noticeService = noticeService;
        this.groupService = groupService;
        this.assignmentService = assignmentService;
        this.fileService = fileService;
        this.projectService = projectService;
    }


    @GetMapping("/test")
    public ResponseResult<Object> test(){
        throw new AccessDeniedException("test");
    }

//    @GetMapping(value = "/notice-list/{project_id}/{page}/{page_size}")
//    public ResponseResult<List<Notice>> getNotices(@PathVariable("project_id") Long projectId,
//                                                @PathVariable("page") long page,
//                                                @PathVariable("page_size") long pageSize,
//                                                   HttpServletRequest request) {
//        // Use the projectId, page, and pageSize in your method
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
//        List<Notice> result = noticeService.getNoticesByStudent(userId, projectId, page, pageSize);
//        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
//    }

//    @PostMapping("/edit_person_info")
//    public ResponseResult<Object> editPersonInfo(
//        HttpServletRequest request,
//        @RequestParam("phone") String phone,
//        @RequestParam("email") String email,
//        @RequestParam("name") String name,
//        @RequestParam("gender") String gender,
//        @RequestParam("birthday") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date birthday,
//        @RequestParam("programmingSkills") List<String> programmingSkills,
//        @RequestParam("avatar") MultipartFile avatar){
//        User user = new User();
//        user.setPhone(phone);
//        user.setEmail(email);
//        user.setName(name);
//        user.setGender(gender);
//        user.setBirthday(birthday);
//        user.setProgrammingSkills(programmingSkills);
//        user.setAvatar(avatar);
//
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        userService.editPersonInfo(user, jwt);
//        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
//    }
//
//    @GetMapping("/get_person_info")
//    public ResponseResult<User> getPersonInfo(HttpServletRequest request){
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//
//        return ResponseResult.ok(userService.getPersonInfo(Long.parseLong(JWTUtil.getUserIdByToken(jwt))), "Success", JWTUtil.updateJWT(jwt));
//
//    }
//
//    @GetMapping("/get_avatar")
//    public ResponseEntity<Resource> getAvatar(HttpServletRequest request) {
//
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
//        Resource rec = fileService.getAvatar(userId);
//        System.err.println(rec.getFilename());
//        return ResponseEntity.ok()
//            .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
//            .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
//            .body(rec);
//    }

    @GetMapping("/intend_teammates/{project_id}")
    public ResponseResult<List<String>> getIntendTeammates(HttpServletRequest request, @PathVariable Long project_id){

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        return ResponseResult.ok(projectService.getIntendedTeammates(project_id, userId), "success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/add_intend_teammates")
    public ResponseResult<Object> addIntendTeammates(
        HttpServletRequest request,
        @RequestBody KeyValueWrapper<Long, String> kvw){

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        projectService.addIntendedTeammates(kvw.getKey(), userId, kvw.getValue());
        return ResponseResult.ok(null, "success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/delete_intend_teammates")
    public ResponseResult<Object> deleteIntendTeammates(
        HttpServletRequest request,
        @RequestBody KeyValueWrapper<Long, String> kvw){

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        projectService.deleteIntendedTeammates(kvw.getKey(), userId, kvw.getValue());
        return ResponseResult.ok(null, "success", JWTUtil.updateJWT(jwt));
    }


    @PostMapping("/recruit_mem")
    public ResponseResult<Object> recruitMem(HttpServletRequest request, @RequestBody KeyValueWrapper<Long, List<Long>> gpId_stuIds){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.recruitMem(gpId_stuIds, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/join_group")
    public ResponseResult<Object> joinGroup(HttpServletRequest request, @RequestBody Long groupId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.joinGroup(groupId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @DeleteMapping("/leave_group")
    public ResponseResult<Object> leaveGroup(HttpServletRequest request, @RequestBody Long projId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.leaveGroup(projId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/ack_invitation")
    public ResponseResult<Object> ackInvitation(HttpServletRequest request, @RequestBody Long noticeId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.ackInvitation(noticeId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/ack_application")
    public ResponseResult<Object> ackApplication(HttpServletRequest request, @RequestBody Long noticeId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.ackApplication(noticeId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/nak_invitation_or_application")
    public ResponseResult<Object> nakInvitationOrApplication(HttpServletRequest request, @RequestBody Long noticeId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.nakInvitationOrApplication(noticeId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }


//    @GetMapping(value = "/ass-list/{project_id}/{page}/{page_size}")
//    public ResponseResult<List<Assignment>> getAssignments(@PathVariable("project_id") Long projectId,
//                                                           @PathVariable("page") long page,
//                                                           @PathVariable("page_size") long pageSize,
//                                                           HttpServletRequest request) {
//        // Use the projectId, page, and pageSize in your method
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
//        List<Assignment> result = assignmentService.getAssignmentsByStu(userId, projectId, page, pageSize);
//        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
//    }

    @GetMapping(value = "/get_ass_file/{assignment_id}/{filename}")
    public ResponseEntity<Resource> getAssFile(@PathVariable("assignment_id") Long assignmentId,
                                               @PathVariable("filename") String filename,
                                               HttpServletRequest request) {

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Resource rec = fileService.getFilesOfAssByStu(userId, assignmentId, filename);
        System.err.println(rec.getFilename());
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
            .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
            .body(rec);
    }

    @PostMapping("/submit_assignment")
    public ResponseResult<Object> submitAssignment(HttpServletRequest request,
                                                   @RequestParam("assignmentId") Long assignmentId,
                                                   @RequestParam("text") String text,
                                                   @RequestParam("files") List<MultipartFile> files
                                                   ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        SubmittedAssignment submittedAssignment = new SubmittedAssignment();
        submittedAssignment.setAssignmentId(assignmentId);
        submittedAssignment.setText(text);
        submittedAssignment.setFiles(files);
        assignmentService.submitAss(
            submittedAssignment,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/remove_submitted_ass")
    public ResponseResult<Object> removeAss(HttpServletRequest request, @RequestBody Long assignmentId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.removeSubmittedAss(
            assignmentId,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/view_sub/{assignment_id}")
    public ResponseResult<SubmittedAssignment> viewSub(HttpServletRequest request, @PathVariable("assignment_id") Long assignmentId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        SubmittedAssignment submittedAssignment = assignmentService.viewSubByStu(
            assignmentId,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(submittedAssignment, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping(value = "/get_submitted_ass_file/{assignment_id}/{filename}")
    public ResponseEntity<Resource> getSubmittedAssFile(
        @PathVariable("assignment_id") Long assignmentId,
        @PathVariable("filename") String filename,
        HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Resource rec = fileService.getFilesOfSubmittedAssByStu(userId, assignmentId, filename);
        System.err.println(rec.getFilename());
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
            .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
            .body(rec);
    }

    @PostMapping("/update_visibility")
    public ResponseResult<Object> updateVisibility(HttpServletRequest request,
                                                   @RequestBody KeyValueWrapper<Long, Boolean[]> group_visibility ) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.updateVisibility(Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                group_visibility.getKey(),group_visibility.getValue());
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/group_leader_transfer")
    public ResponseResult<Object> groupLeaderTransfer(HttpServletRequest request,
                                                    @RequestBody KeyValueWrapper<Long, Long> group_member ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.groupLeaderTransfer(Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                group_member.getKey(),group_member.getValue());
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/remove_member")
    public ResponseResult<Object> removeMember(HttpServletRequest request,
                                                      @RequestBody KeyValueWrapper<Long, Long> group_member ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.removeMember(Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                group_member.getKey(),group_member.getValue());
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
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
//            groupService.stuJoinGroup(group_id, stu_id);
            return "join successful";
        }else {
            return "you have joined another group";
        }
    }

    @PostMapping("/updateGroupName/{group_name}/{group_id}")
    //修改某一小组的小组名
    public void updateGroupName(@PathVariable String group_name,
                                @PathVariable long group_id) {
        //groupService.updateGroupName(group_name, group_id);
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

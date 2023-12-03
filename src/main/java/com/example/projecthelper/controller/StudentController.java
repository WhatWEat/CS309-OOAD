package com.example.projecthelper.controller;

import com.example.projecthelper.entity.*;
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
import com.example.projecthelper.util.Wrappers.ObjectWrapper;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

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



    @PostMapping("/add_intend_teammates")
    public ResponseResult<Object> addIntendTeammates(
        HttpServletRequest request,
        @RequestBody KeyValueWrapper<Long, String> kvw){

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        projectService.addIntendedTeammates(kvw.getKey(), userId, kvw.getValue());
        return ResponseResult.ok(null, "success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/delete_intend_teammates/{projId}/{value}")
    public ResponseResult<Object> deleteIntendTeammates(
        HttpServletRequest request,
        @PathVariable Long projId,
        @PathVariable String value
    ){

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        projectService.deleteIntendedTeammates(projId, userId, value);
        return ResponseResult.ok(null, "success", JWTUtil.updateJWT(jwt));
    }


    @GetMapping("/get_stu_not_in_group/{project_id}")
    public ResponseResult<List<User>> getStuNotInGroup(HttpServletRequest request,
                                                   @PathVariable("project_id") Long pjId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<User> users = groupService.getStuNotInGroup(pjId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(users, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/recruit_mem")
    public ResponseResult<Object> recruitMem(HttpServletRequest request, @RequestBody KeyValueWrapper<Long, Notice> gpId_notice){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.recruitMem(gpId_notice, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/apply_to_join_group")
    public ResponseResult<Object> applyToJoinGroup(HttpServletRequest request, @RequestBody KeyValueWrapper<Long, Notice> gpId_notice){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.applyToJoinGroup(gpId_notice, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/group_leader_transfer")
    public ResponseResult<Object> groupLeaderTransfer(HttpServletRequest request, @RequestBody KeyValueWrapper<Long, Notice> gpId_notice){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.transferLeader(gpId_notice,Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/remove_member")
    public ResponseResult<Object> removeMember(HttpServletRequest request, @RequestBody KeyValueWrapper<Long, Notice> gpId_notice){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.removeMen(gpId_notice,Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/join_group")
    public ResponseResult<Object> joinGroup(HttpServletRequest request, @RequestBody Long groupId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.joinGroup(groupId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @GetMapping("/group_members/{project_id}")
    public ResponseResult<List<User>> getGpMem(HttpServletRequest request, @PathVariable("project_id") Long pjId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<User> result = groupService.getGpMem(Long.parseLong(JWTUtil.getUserIdByToken(jwt)), pjId);
        return ResponseResult.ok(result, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/get_group_id/{project_id}")
    public ResponseResult<KeyValueWrapper<Long, Boolean>> getGroupId(HttpServletRequest request, @PathVariable("project_id") Long pjId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Group gp = groupService.findGroupOfStuInProject(Long.parseLong(JWTUtil.getUserIdByToken(jwt)), pjId);
        long result = gp == null ? -1 : gp.getGroupId();
        boolean isLeader =
            gp != null && gp.getLeaderId() == Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        return ResponseResult.ok(new KeyValueWrapper<>(result, isLeader), "Success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/leave_group/{project_id}")
    public ResponseResult<Object> leaveGroup(HttpServletRequest request, @PathVariable("project_id") Long projId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.leaveGroup(projId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/ack_invitation")
    public ResponseResult<Object> ackInvitation(HttpServletRequest request, @RequestBody
    ObjectWrapper<Long> noticeId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.ackInvitation(noticeId.getObject(), Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/ack_application")
    public ResponseResult<Object> ackApplication(HttpServletRequest request, @RequestBody ObjectWrapper<Long> noticeId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.ackApplication(noticeId.getObject(), Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/nak_invitation_or_application")
    public ResponseResult<Object> nakInvitationOrApplication(HttpServletRequest request, @RequestBody ObjectWrapper<Long> noticeId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.nakInvitationOrApplication(noticeId.getObject(), Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
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
        Resource rec = fileService.getFilesOfAssByStu(userId, assignmentId, filename, false);
        System.err.println(rec.getFilename());
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
            .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
            .body(rec);
    }

    @GetMapping(value = "/get_ass_file_pdf_version/{assignment_id}/{filename}")
    public ResponseEntity<Resource> getAssPdfFile(@PathVariable("assignment_id") Long assignmentId,
                                               @PathVariable("filename") String filename,
                                               HttpServletRequest request) {

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Resource rec = fileService.getFilesOfAssByStu(userId, assignmentId, filename, true);
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

    @PostMapping("/submit_evaluation")
    public ResponseResult<Object> submitEvaluation(HttpServletRequest request,
                                                   @RequestParam("assignmentId") Long assignmentId,
                                                   @RequestParam("content") String content,
                                                   @RequestParam("grade") Float grade,
                                                   @RequestParam("commentedGroup") Long commentedGroup
                                                   ){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Evaluation evaluation = new Evaluation();
        evaluation.setAssignmentId(assignmentId);
        evaluation.setContent(content);
        evaluation.setGrade(grade);
        evaluation.setCommentedGroup(commentedGroup);
        assignmentService.submitEva(
                evaluation,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/remove_submitted_ass/{assignmentId}")
    public ResponseResult<Object> removeAss(HttpServletRequest request, @PathVariable("assignmentId") Long assignmentId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.removeSubmittedAss(
            assignmentId,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }
    @DeleteMapping("/remove_eva/{assignmentId}")
    public ResponseResult<Object> removeEva(HttpServletRequest request, @PathVariable("assignmentId") Long assignmentId,
                                            @PathVariable("commentedgroup") Long commentedgroup){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.removeEva(
                assignmentId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                commentedgroup
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

    @GetMapping("/view_eva/{assignment_id}")
    public ResponseResult<Double> viewEva(HttpServletRequest request, @PathVariable("assignment_id") Long assignmentId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Double eva = assignmentService.viewEvaByStu(
                assignmentId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(eva, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/to_comment/{assignment_id}")
    public ResponseResult<List<Long>> selectToComment(HttpServletRequest request, @PathVariable("assignment_id") Long assignmentId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<Long> groups = assignmentService.selectToCommented(
                assignmentId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(groups, "Success", JWTUtil.updateJWT(jwt));
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


    @GetMapping("/GradeBook/{project_id}/{page}/{pageSize}")
    public ResponseResult<List<SubmittedAssignment>> GradeBook(
            @PathVariable("project_id") Long projId,
            @PathVariable("page") Integer page,
            @PathVariable("pageSize") Integer pageSize,
            HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<SubmittedAssignment> out = assignmentService.getStuAllSub(projId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                page,pageSize
                );
        return ResponseResult.ok(out, "Success", JWTUtil.updateJWT(jwt));
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

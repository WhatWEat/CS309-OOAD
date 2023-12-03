package com.example.projecthelper.controller;

import com.example.projecthelper.entity.*;
import com.example.projecthelper.service.*;
import com.example.projecthelper.util.*;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import com.example.projecthelper.util.Wrappers.ObjectCountWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/tea")
public class TeacherController {
    private final AuthService authService;
    private final NoticeService noticeService;
    private final GroupService groupService;
    private final AssignmentService assignmentService;
    private final FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    public TeacherController(AuthService authService, NoticeService noticeService,
                             GroupService groupService, AssignmentService assignmentService,
                             FileService fileService) {
        this.authService = authService;
        this.noticeService = noticeService;
        this.groupService = groupService;
        this.assignmentService = assignmentService;
        this.fileService = fileService;
    }

    @PostMapping("/create_multiple_users")
    public ResponseResult<Object> createMultipleUsers(@RequestBody KeyValueWrapper<ObjectCountWrapper<User>, String> multiUsersAndPass, HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        authService.registerUserByTea(multiUsersAndPass.getKey(), new KeyValueWrapper<>(JWTUtil.getUserIdByToken(jwt), multiUsersAndPass.getValue()));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }


    @PostMapping("/create_project")
    public ResponseResult<Object> createProject(@RequestBody Project proj, HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        proj.setTeacherId(Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        projectService.createProject(proj);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/add_stu_to_project")
    public ResponseResult<Object> addStuToProject(HttpServletRequest request, @RequestBody
    KeyValueWrapper<Long, List<Long>> pjId_stuId) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        projectService.addStuToProject(pjId_stuId.getKey(), pjId_stuId.getValue(), Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/edit_project")
    public ResponseResult<Object> editProj(HttpServletRequest request, @RequestBody Project project) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        projectService.editProject(project, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

//    @GetMapping(value = "/notice-list/{project_id}/{page}/{page_size}")
//    public ResponseResult<List<Notice>> getNotices(@PathVariable("project_id") Long projectId,
//                                                   @PathVariable("page") long page,
//                                                   @PathVariable("page_size") long pageSize,
//                                                   HttpServletRequest request) {
//        // Use the projectId, page, and pageSize in your method
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
//        List<Notice> result = noticeService.getNoticesByTeacher(userId, projectId, page, pageSize);
//        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
//    }

    @PostMapping("/post_notice")
    public ResponseResult<Object> postNotice(@RequestBody Notice notice, HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        System.err.println(notice);
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
    public ResponseResult<Object> modifyNotice(HttpServletRequest request, @RequestBody Notice notice) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        noticeService.modifyNoticeWithUser(
                notice,
                ntId -> {
                    Notice ntc = noticeService.findNoticeById(ntId);
                    return Objects.equals(
                            projectService.findTeacherByProject(ntc.getProjectId()),
                            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
                    ) && ntc.getType() == 0;
                }
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/delete_notice")
    public ResponseResult<Object> deleteNotice(HttpServletRequest request, @RequestBody Long noticeId) {
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


    @GetMapping("/get_ta_list_of_proj/{proj_id}")
    public ResponseResult<List<User>> getTaListOfProj(HttpServletRequest request, @PathVariable("proj_id") Long projId) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<User> users = groupService.getTaListOfProj(projId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(users, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/get_stu_list_of_proj/{proj_id}/{page}/{page_size}")
    public ResponseResult<List<User>> getStuListOfProj(
            HttpServletRequest request,
            @PathVariable("proj_id") Long projId,
            @PathVariable("page") int page,
            @PathVariable("page_size") int page_size) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<User> users = groupService.getStuListOfProj(projId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)), page, page_size);
        return ResponseResult.ok(users, "Success", JWTUtil.updateJWT(jwt));
    }


    @PostMapping("/create_group")
    public ResponseResult<Object> createGroup(HttpServletRequest request, @RequestBody Group gp) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);

        Long id = groupService.createGroup(
                gp,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                pjId -> Objects.equals(
                        projectService.findTeacherByProject(pjId),
                        Long.parseLong(JWTUtil.getUserIdByToken(jwt))
                )
        );
        return ResponseResult.ok(id, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/create_multiple_groups")
    public ResponseResult<List<Long>> createMultipleGroup(HttpServletRequest request, @RequestBody ObjectCountWrapper<Group> ocw) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);

        List<Long> ids = groupService.createGroup(
                ocw,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                pjId -> Objects.equals(
                        projectService.findTeacherByProject(pjId),
                        Long.parseLong(JWTUtil.getUserIdByToken(jwt))
                )
        );
        return ResponseResult.ok(ids, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/modify_group_info")
    public ResponseResult<Object> modifyGroupInfo(HttpServletRequest request, @RequestBody Group group) {
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

    @DeleteMapping("/delete_group/{groupId}")
    public ResponseResult<Object> modifyGroupInfo(HttpServletRequest request, @PathVariable("groupId") Long groupId) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.deleteGroupForTea(
                groupId, Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/set_group")
    public ResponseResult<Object> setGroup(HttpServletRequest request, @RequestBody Group group) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.updateAllGroupForTea(
                group,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

//    @GetMapping(value = "/ass-list/{project_id}/{page}/{page_size}")
//    public ResponseResult<List<Assignment>> getAssignments(@PathVariable("project_id") Long projectId,
//                                                   @PathVariable("page") long page,
//                                                   @PathVariable("page_size") long pageSize,
//                                                   HttpServletRequest request) {
//        // Use the projectId, page, and pageSize in your method
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
//        List<Assignment> result = assignmentService.getAssignmentsByTea(userId, projectId, page, pageSize);
//        System.err.println(result.get(0).getFilePaths());
//        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
//    }

    @GetMapping(value = "/get_ass_file/{assignment_id}/{filename}")
    public ResponseEntity<Resource> getAssFile(@PathVariable("assignment_id") Long assignmentId,
                                               @PathVariable("filename") String filename,
                                               HttpServletRequest request) {

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Resource rec = fileService.getFilesOfAssByTeaOrTa(userId, assignmentId, filename, Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt)));
        System.err.println(rec.getFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
                .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
                .body(rec);
    }

    @PostMapping("/post_assignment")
    public ResponseResult<Object> postAssignment(HttpServletRequest request,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("projectId") Long projectId,
                                                 @RequestParam("fullMark") Integer fullMark,
                                                 @RequestParam("type") String type,
                                                 @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                 LocalDateTime deadline,
                                                 @RequestParam("requireExtension") String requireExtension,
                                                 @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setProjectId(projectId);
        assignment.setFullMark(fullMark);
        assignment.setType(type);
        assignment.setDeadline(deadline);
        assignment.setFiles(files);
        assignment.setRequireExtension(requireExtension);

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.createAss(
                assignment,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                pjId -> Objects.equals(
                        projectService.findTeacherByProject(pjId),
                        Long.parseLong(JWTUtil.getUserIdByToken(jwt))
                )
        );
        System.err.println("assId" + assignment.getAssignmentId());

        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));

    }

    @DeleteMapping("delete_ass")
    public ResponseResult<Object> deleteAss(@RequestBody Long assId, HttpServletRequest request) {

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        fileService.removeFilesOfAss(Long.parseLong(JWTUtil.getUserIdByToken(jwt)), assId);
        assignmentService.deleteAss(assId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)), Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/view_all_submitted_ass/{assignment_id}/{page}/{page_size}")
    public ResponseResult<List<SubmittedAssignment>> viewAllSubmittedAss(
            HttpServletRequest request,
            @PathVariable("assignment_id") Long assignmentId,
            @PathVariable("page") Long page,
            @PathVariable("page_size") Long page_size) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<SubmittedAssignment> submittedAssignments = assignmentService.viewAllSub(
                assignmentId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                page,
                page_size,
                Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))
        );
        return ResponseResult.ok(submittedAssignments, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/view_evaluation/{assignment_id}/{submitid}/{togroup}/{grade}")
    public ResponseResult<List<Evaluation>> viewEva(
            HttpServletRequest request,
            @PathVariable("assignment_id") Long assignmentId,
            @PathVariable("grade") Float grade,
            @PathVariable("submitid") Long submitid,
            @PathVariable("togroup") Long togroup) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);

        List<Evaluation> evaluations = assignmentService.viewEva(
                assignmentId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                grade,
                submitid,
                togroup,
                Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt))
        );
        return ResponseResult.ok(evaluations, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping(value = "/get_submitted_ass_file/{assignment_id}/{stu_id}/{filename}")
    public ResponseEntity<Resource> getSubmittedAssFile(
            @PathVariable("assignment_id") Long assignmentId,
            @PathVariable("stu_id") Long stuId,
            @PathVariable("filename") String filename,
            HttpServletRequest request) {

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Resource rec = fileService.getFilesOfSubmittedAssByTeaOrTa(userId, stuId, assignmentId, filename, Integer.parseInt(JWTUtil.getIdentityCodeByToken(jwt)));
        System.err.println(rec.getFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
                .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
                .body(rec);
    }

    @PostMapping("/grade_ass")
    public ResponseResult<Object> gradeAss(HttpServletRequest request, @RequestBody SubmittedAssignment submittedAssignment) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.gradeAss(
                submittedAssignment,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                Integer.parseInt(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/grade_ass_with_file")
    public ResponseResult<Object> gradeAssWithFile(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file,
            @RequestParam("assignmentId") Long assignmentId) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.gradeAssWithFile(
                file, assignmentId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                Integer.parseInt(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));

    }

    //PART IV: select ta
    @GetMapping("/ta_list/{project_id}")
    public ResponseResult<List<User>> getTaList(
            @PathVariable("project_id") Long projId,
            HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        List<User> users = userService.getUsersByIdentity(IdentityCode.TEACHER_ASSISTANT.getValue(), projId);
        return ResponseResult.ok(users, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/designate_ta_to_proj")
    public ResponseResult<Object> designateTaToProj(
            @RequestBody KeyValueWrapper<Long, List<Long>> pjTaId,
            HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        projectService.designateTaToProj(pjTaId.getKey(), pjTaId.getValue(), Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));

    }

    @PostMapping("/update_group_leader")
    public ResponseResult<Object> updateGroupLeader(HttpServletRequest request,
                                                    @RequestBody KeyValueWrapper<Long, Long> leader_group) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.updateLeader(leader_group.getKey(), leader_group.getValue(), gpId -> Objects.equals(
                groupService.findCreatorByGroup(gpId),
                Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        ));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @DeleteMapping("/remove_ta_from_proj")
    public ResponseResult<Object> removeTaFromProj(
            KeyValueWrapper<Long, Long> pjTaId,
            HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        projectService.removeTaFromProj(pjTaId.getKey(), pjTaId.getValue(), Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/update_visibility")
    public ResponseResult<Object> updateVisibility(HttpServletRequest request,
                                                   @RequestBody KeyValueWrapper<Long, Boolean[]> group_visibility) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        groupService.updateVisibility(Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                group_visibility.getKey(), group_visibility.getValue());
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    //    @GetMapping("/getIfStuSub")
//    public ResponseResult<Object> getIfStuSub(HttpServletRequest request, @RequestBody KeyValueWrapper assignment){
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        boolean sub = assignmentService.ifStuSub()
//        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
//    }
    @GetMapping("/allGradeBook/{project_id}")
    public ResponseResult<HashMap<Long,List<SubmittedAssignment>>> allGradeBook(
            @PathVariable("project_id") Long projId,
            HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        HashMap<Long,List<SubmittedAssignment>> out = assignmentService.getProAllSub(projId,
                Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
                pjId -> Objects.equals(
                        projectService.findTeacherByProject(pjId),
                        Long.parseLong(JWTUtil.getUserIdByToken(jwt))));
        return ResponseResult.ok(out, "Success", JWTUtil.updateJWT(jwt));
    }


    /**
     * 数据库功能测试
     */

    //test用
    @GetMapping(value = "/get_name_by_id/{user_id}")
    public ResponseResult<String> getNameById(@PathVariable("user_id") Long userId, HttpServletRequest request) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        String name = userService.getPersonName(userId);
        return ResponseResult.ok(name, "success", JWTUtil.updateJWT(jwt));
    }


}

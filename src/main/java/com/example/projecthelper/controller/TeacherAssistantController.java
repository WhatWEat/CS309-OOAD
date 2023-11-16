package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Notice;
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
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TeacherAssistantController {

    private final AuthService authService;
    private final NoticeService noticeService;
    private final GroupService groupService;
    private final AssignmentService assignmentService;
    private final FileService fileService;

    private final UserService userService;

    private final ProjectService projectService;

    @Autowired
    public TeacherAssistantController(AuthService authService, NoticeService noticeService,
                                      GroupService groupService, AssignmentService assignmentService,
                                      FileService fileService, UserService userService,
                                      ProjectService projectService) {
        this.authService = authService;
        this.noticeService = noticeService;
        this.groupService = groupService;
        this.assignmentService = assignmentService;
        this.fileService = fileService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping(value = "/notice-list/{project_id}/{page}/{page_size}")
    public ResponseResult<List<Notice>> getNotices(@PathVariable("project_id") Long projectId,
                                                   @PathVariable("page") long page,
                                                   @PathVariable("page_size") long pageSize,
                                                   HttpServletRequest request) {
        // Use the projectId, page, and pageSize in your method
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        List<Notice> result = noticeService.getNoticesByTa(userId, projectId, page, pageSize);
        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/post_notice")
    public ResponseResult<Object> postNotice(@RequestBody Notice notice, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        System.err.println(notice);
        noticeService.postNotice(
            notice,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            pjId -> Objects.equals(
                projectService.checkTaInProj(pjId, Long.parseLong(JWTUtil.getUserIdByToken(jwt))),
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


    @GetMapping(value = "/ass-list/{project_id}/{page}/{page_size}")
    public ResponseResult<List<Assignment>> getAssignments(@PathVariable("project_id") Long projectId,
                                                           @PathVariable("page") long page,
                                                           @PathVariable("page_size") long pageSize,
                                                           HttpServletRequest request) {
        // Use the projectId, page, and pageSize in your method
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        List<Assignment> result = assignmentService.getAssignmentsByTa(userId, projectId, page, pageSize);
        System.err.println(result.get(0).getFilePaths());
        return ResponseResult.ok(result, "success", JWTUtil.updateJWT(jwt));
    }

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
                                                 @RequestParam("files") List<MultipartFile> files) {
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
            pjId -> !Objects.isNull(
                projectService.checkTaInProj(pjId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)))
            )
        );
        System.err.println("assId"+assignment.getAssignmentId());

        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));

    }

    @DeleteMapping("delete_ass")
    public ResponseResult<Object> deleteAss(@RequestBody Long assId, HttpServletRequest request){

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        fileService.removeFilesOfAss(Long.parseLong(JWTUtil.getUserIdByToken(jwt)), assId);
        assignmentService.deleteAss(assId, Long.parseLong(JWTUtil.getUserIdByToken(jwt)), Integer.parseInt(
            JWTUtil.getIdentityCodeByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping("/view_all_submitted_ass/{assignment_id}/{page}/{page_size}")
    public ResponseResult<List<SubmittedAssignment>> viewAllSubmittedAss(
        HttpServletRequest request,
        @PathVariable("assignment_id") Long assignmentId,
        @PathVariable("page") Long page,
        @PathVariable("page_size") Long page_size){
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
    public ResponseResult<Object> gradeAss(HttpServletRequest request, @RequestBody SubmittedAssignment submittedAssignment){
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
        @RequestParam("assignmentId") Long assignmentId){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        assignmentService.gradeAssWithFile(
            file, assignmentId,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt)),
            Integer.parseInt(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));

    }


}

package com.example.projecthelper.controller;

import com.example.projecthelper.entity.Project;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.service.FileService;
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
    private final ProjectService projectService;
    private final static Logger log = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    public UserController(AuthService authService, UserService userService,
                          FileService fileService, ProjectService projectService) {
        this.authService = authService;
        this.userService = userService;
        this.fileService = fileService;
        this.projectService = projectService;
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


}

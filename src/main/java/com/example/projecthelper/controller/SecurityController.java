package com.example.projecthelper.controller;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.security.CustomJwtAuthenticationTokenFilter;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.service.FileService;
import com.example.projecthelper.service.UserService;
import com.example.projecthelper.util.FileUtil;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class SecurityController {
    private final AuthService authService;
    private final UserService userService;
    private final FileService fileService;

    private final static Logger log = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    public SecurityController(AuthService authService, UserService userService,
                              FileService fileService) {
        this.authService = authService;
        this.userService = userService;
        this.fileService = fileService;
    }


    @GetMapping("/test")
    // 会自动跳转到/login
    public String get(){
        return "hello231313";
    }

    @PostMapping("/t1")
    // 会自动跳转到/login
    public String post(){
        return "hello231313";
    }



    @GetMapping("/login")
    public ResponseResult<Object> login_test(HttpServletRequest request){
        System.out.println(request);
        return ResponseResult.ok(null, "原神，启动", JWTUtil.createJWT("12110000", "3"));
    }

    @GetMapping("/signup")
    public String signup_test(){
        log.info("test, log successful");
        return "signup";
    }


    @PostMapping("/edit_personal_info")
    public ResponseResult<Object> editPersonalInfo(
        HttpServletRequest request,
        @RequestParam("name") String name,
        @RequestParam("gender") String gender,
        @RequestParam("birthday") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date birthday,
        @RequestParam(value = "programmingSkills", required = false) List<String> programmingSkills,
        @RequestParam(value = "avatar", required = false) MultipartFile avatar){
        User user = new User();
        user.setName(name);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setProgrammingSkills(programmingSkills);
        user.setAvatar(avatar);

        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        userService.editPersonInfo(user, jwt);
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping({"/get_personal_info/{search_id}", "/get_personal_info"})
    public ResponseResult<User> getPersonalInfo(
        HttpServletRequest request,
        @PathVariable(required = false) Long search_id){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Long searchId = search_id == null ? userId: search_id;
        User user = userService.getPersonInfo(searchId);
        return ResponseResult.ok(user, "success", JWTUtil.updateJWT(jwt));
    }

    @GetMapping({"/get_avatar/{search_id}", "/get_avatar"})
    public ResponseEntity<Resource> getAvatar(HttpServletRequest request,
                                              @PathVariable(required = false) Long search_id) {
        System.err.println("search_id"+search_id);
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
        Long searchId = search_id == null ? userId: search_id;
        Resource rec = fileService.getAvatar(searchId);
        System.err.println(rec.getFilename());
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
            .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
            .body(rec);
    }

    @PostMapping("/change_password")
    public ResponseResult<Object> changePassword(HttpServletRequest request, @RequestBody KeyValueWrapper<String, KeyValueWrapper<String, String>> kvw){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        authService.changePass(Long.parseLong(JWTUtil.getUserIdByToken(jwt)), kvw.getKey(), kvw.getValue().getKey(), kvw.getValue().getValue());
        return ResponseResult.ok(null, "success", JWTUtil.updateJWT(jwt));
    }
    @PostMapping("/signup")
    public ResponseResult<Long> signup(@RequestBody User user){
        String jwt = authService.registerUser(user);
        return ResponseResult.ok(null, "", jwt);
    }

    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody KeyValueWrapper<String, String> userPass){
        String jwt = authService.login(userPass);
        //NOTE: 校验失败后，spring-security自动跑出并捕获AuthenticationException异常
        return ResponseResult.ok(null, "success", jwt);
    }

    //登出：清理token
    @DeleteMapping("/logout")
    public ResponseResult<Object> logout(HttpServletRequest request){
        //NOTE: 如果还没登陆
        String token = HTTPUtil.getHeader(request, CustomJwtAuthenticationTokenFilter.AUTH_HEADER);
        if(token == null || !JWTUtil.verifyToken(token))
            return ResponseResult.ok(null, "登出成功", null);
        //NOTE：如果已经登陆了，要拉黑名单
        return ResponseResult.ok(null, "登出成功", authService.logout(JWTUtil.getUserIdByToken(token)));
    }




    @GetMapping("/id")
    public ResponseResult<String> getId(HttpServletRequest request){
        String jwt = null;

        return ResponseResult.ok(null, "success", null);

    }



    /** 数据库功能测试
     *
     */

    @PostMapping("/request_code")
    public ResponseResult<Object> testMail(@RequestBody String address) {

        System.out.println(address);


        userService.sendMail(address);

        return ResponseResult.ok(null, "Success", null);
    }

    @PostMapping("/login_with_email_code")
    public ResponseResult<Object> testCode(
        @RequestBody KeyValueWrapper<String, String> address_code
    ) {
        String jwt = userService.checkCode(address_code.getValue(), address_code.getKey());
        return ResponseResult.ok(null, "Success", jwt);
    }
}

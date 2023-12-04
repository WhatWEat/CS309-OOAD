package com.example.projecthelper.controller;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.security.CustomJwtAuthenticationTokenFilter;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.service.FileService;
import com.example.projecthelper.service.UserService;
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
import org.springframework.format.annotation.DateTimeFormat;
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
    public String signup_test() throws Exception {
        return "signup";
    }


    @PostMapping("/edit_personal_info")
    public ResponseResult<Object> editPersonalInfo(
        HttpServletRequest request,
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("gender") String gender,
        @RequestParam("birthday") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date birthday,
        @RequestParam(value = "programmingSkills", required = false) List<String> programmingSkills,
        @RequestParam(value = "avatar", required = false) MultipartFile avatar){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
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

//    @GetMapping({"/get_avatar/{search_id}", "/get_avatar"})
//    public ResponseEntity<Resource> getAvatar(HttpServletRequest request,
//                                              @PathVariable(required = false) Long search_id) {
//        System.err.println("search_id"+search_id);
//        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
//        Long userId = Long.parseLong(JWTUtil.getUserIdByToken(jwt));
//        Long searchId = search_id == null ? userId: search_id;
//        Resource rec = fileService.getAvatar(searchId);
//        System.err.println(rec.getFilename());
//        return ResponseEntity.ok()
//            .contentType(MediaType.parseMediaType(FileUtil.getMIMEType(rec.getFilename())))
//            .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtil.declareAttachment(rec.getFilename()))
//            .body(rec);
//    }

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


        userService.request_code(address);


        return ResponseResult.ok(null, "Success", null);
    }

    @PostMapping("/request_massage")
    public ResponseResult<Object> testPhone(@RequestBody String phone) {

        System.out.println(phone);

        userService.request_phone(phone);

        return ResponseResult.ok(null, "Success", null);
    }

    @PostMapping("/login_with_email_code")
    public ResponseResult<Object> testCode(
        @RequestBody KeyValueWrapper<String, String> address_code
    ) {
        String jwt = userService.login_with_email(address_code.getValue(), address_code.getKey());
        return ResponseResult.ok(null, "Success", jwt);
    }

    @PostMapping("/login_with_phone_code")
    public ResponseResult<Object> testPhone(
            @RequestBody KeyValueWrapper<String, String> pone_code
    ) {
        String jwt = userService.login_with_phone(pone_code.getValue(), pone_code.getKey());
        return ResponseResult.ok(null, "Success", jwt);
    }

    //FUNC: 重置密码
    @PostMapping("/get_forget_password_code")
    public ResponseResult<Object> get_forget_password_code(
        @RequestBody KeyValueWrapper<Integer, String> type_number
    ) {
        userService.getForgetPassCode(type_number);
        return ResponseResult.ok(null, "Success", null);
    }

//    @PostMapping("/login_with_message_code")
//    public ResponseResult<Object> testMassageCode(
//            @RequestBody KeyValueWrapper<String, String> phone_code
//    ) {
//        String jwt = userService.checkCodeMassage(phone_code.getValue(), phone_code.getKey());
//        return ResponseResult.ok(null, "Success", jwt);
//    }

    @PostMapping("/change_forget_password")
    public ResponseResult<Object> change_forget_password(
        @RequestBody KeyValueWrapper<KeyValueWrapper<Integer, String>, KeyValueWrapper<String, String>> type_pass_num_code
    ) {
        String jwt = userService.change_forget_password(type_pass_num_code);
        return ResponseResult.ok(null, "Success", jwt);
    }
    //FUNC: 修改手机和邮箱
    @PostMapping("/get_edit_code")
    public ResponseResult<Object> get_edit_code(
        @RequestBody KeyValueWrapper<Integer, String> type_number, HttpServletRequest request
    ) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        userService.sendCodeToChangeNumber(type_number, Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/verify_edit_code")
    public ResponseResult<Object> verify_edit_code (
        @RequestBody KeyValueWrapper<Integer, KeyValueWrapper<String, String>> type_num_code,
        HttpServletRequest request
    ) {
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        userService.verify_modify_code(
            type_num_code,
            Long.parseLong(JWTUtil.getUserIdByToken(jwt))
        );
        return ResponseResult.ok(null, "Success", jwt);
    }





}

package com.example.projecthelper.controller;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.service.AuthService;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import com.example.projecthelper.util.Wrappers.ObjectCountWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/adm")
public class AdministratorController {

    @Autowired
    AuthService authService;

    @GetMapping("/test")
    public ResponseResult<Object> test(){
        return ResponseResult.ok(null, "Success", null);
    }

    @PostMapping("/create_multiple_users")
    public ResponseResult<Object> createMultipleUsers(@RequestBody
                                                      KeyValueWrapper<ObjectCountWrapper<User>, String> multiUsersAndPass, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        authService.registerUserByAdm(multiUsersAndPass.getKey(), new KeyValueWrapper<>(JWTUtil.getUserIdByToken(jwt), multiUsersAndPass.getValue()));
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PostMapping("/create_multiple_users_with_file")
    public ResponseResult<Object> createMultipleUsersWithFile(
        HttpServletRequest request,
        @RequestParam("password") String password,
        @RequestParam("file") MultipartFile file){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        authService.createMultipleUsersWithFile(
            file,
            new KeyValueWrapper<>(JWTUtil.getUserIdByToken(jwt), password)
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/reset_pass_for_multiple_users")
    public ResponseResult<Object> resetPassForMultiUsers(@RequestBody
                                                      KeyValueWrapper<String, KeyValueWrapper<String, List<Long>>> multiUsersIdAndPass, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        authService.resetPassForMultiUsers(
            multiUsersIdAndPass.getValue(),
            new KeyValueWrapper<>(JWTUtil.getUserIdByToken(jwt), multiUsersIdAndPass.getKey())
        );

        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/freeze_multiple_users")
    public ResponseResult<Object> freezeMultiUsers(@RequestBody
                                                        KeyValueWrapper<List<Long>, String> multiUsersIdAndPass, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);
        authService.freezeMultiUsers(
            multiUsersIdAndPass.getKey(),
            new KeyValueWrapper<>(JWTUtil.getUserIdByToken(jwt), multiUsersIdAndPass.getValue())
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }

    @PutMapping("/unfreeze_multiple_users")
    public ResponseResult<Object> unfreezeMultiUsers(@RequestBody
                                                        KeyValueWrapper<List<Long>, String> multiUsersIdAndPass, HttpServletRequest request){
        String jwt = HTTPUtil.getHeader(request, HTTPUtil.TOKEN_HEADER);

        authService.unfreezeMultiUsers(
            multiUsersIdAndPass.getKey(),
            new KeyValueWrapper<>(JWTUtil.getUserIdByToken(jwt), multiUsersIdAndPass.getValue())
        );
        return ResponseResult.ok(null, "Success", JWTUtil.updateJWT(jwt));
    }




    /** 数据库功能测试
     *
     */

}

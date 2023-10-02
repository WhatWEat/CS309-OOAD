package com.example.projecthelper.service;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.util.JWTUtil;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    //TODO:更新个人信息
    public boolean editPersonInfo(User user, String jwt){
        if(user.getUser_id().toString().equals(JWTUtil.getUserIdByToken(jwt))){
            return true;
        }
        return false;
    }
}

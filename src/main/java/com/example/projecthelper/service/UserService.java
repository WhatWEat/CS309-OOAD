package com.example.projecthelper.service;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersMapper usersMapper;
    //TODO:更新个人信息
    public boolean editPersonInfo(User user, String jwt){
        if(user.getUser_id().toString().equals(JWTUtil.getUserIdByToken(jwt))){
            usersMapper.updateStuInformation(user.getTechnology_stack(), user.getProgramming_skills(),user.getIntended_teammates(), user.getUser_id());
            return true;
        }
        return false;
    }



}

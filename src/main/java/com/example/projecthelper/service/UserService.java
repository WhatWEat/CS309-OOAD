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
            return true;
        }
        return false;
    }
    public void updateStuInformation(String technology_stack, String programming_skills,String intended_teammates, long user_id){
        usersMapper.updateStuInformation(technology_stack,programming_skills,intended_teammates,user_id);
    }

    public long registerUser(String identity, String password, String name, String gender){
        User user = new User(identity, password, name, gender);
        usersMapper.registerUser(user);
        return user.getUser_id();
    }
}

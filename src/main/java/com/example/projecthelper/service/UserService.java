package com.example.projecthelper.service;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersMapper usersMapper;

    public void updateStuInformation(String technology_stack, String programming_skills,String intended_teammates, long user_id){
        usersMapper.updateStuInformation(technology_stack,programming_skills,intended_teammates,user_id);
    }

    public long registerUser(String identity, String password, String name, String gender){
        User user = new User(identity, password, name, gender);
        usersMapper.registerUser(user);
        return user.getUser_id();
    }
}

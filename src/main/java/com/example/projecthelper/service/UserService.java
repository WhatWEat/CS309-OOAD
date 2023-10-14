package com.example.projecthelper.service;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.JWTUtil;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersMapper usersMapper;

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    //TODO:更新个人信息
    public void editPersonInfo(User user, String jwt){
        try {
            usersMapper.updateStuInformation(user.getTechnologyStack(), user.getProgrammingSkills(),user.getIntendedTeammates(), Integer.parseInt(JWTUtil.getUserIdByToken(jwt)));
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }


    
    // 数据库功能测试
//    public long registerUser(int identity, String password, String name, String gender){
//        User user = new User(identity, password, name, gender);
//        usersMapper.registerUser(user);
//        return user.getUser_id();
//    }

}

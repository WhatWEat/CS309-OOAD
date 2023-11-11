package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.FileUtil;
import com.example.projecthelper.util.JWTUtil;
import java.util.List;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private FileService fileService;

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    //TODO:更新个人信息
    public void editPersonInfo(User user, String jwt){
        //此处mapper中传参已经改为user，可以更改名字、身份、id、性别外的所有信息
        try {
            user.setUserId(Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
            fileService.removeOriAvatar(Long.parseLong(JWTUtil.getUserIdByToken(jwt)));
            if(user.getAvatar() != null){

                String path = FileUtil.generateAvatarPath(user.getUserId());
                String avP = FileUtil.saveFile(path, user.getAvatar().getOriginalFilename(), user.getAvatar());
                user.setAvatarPath(avP);
            }
            usersMapper.updateStuInformation(user);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getPersonInfo(Long userId){
        User user = usersMapper.findUserById(userId);
        System.err.println(user);
        if(user != null){
            user.setAvatarPath(null);
            user.setPassword(null);
        }
        else
            throw new InvalidFormException("找不到用户");
        return user;
    }

    public List<User> getUsersByIdentity(int identity, int page, int page_size){
        return usersMapper.findUsersById(identity, page_size, page * page_size);

    }


    
    // 数据库功能测试
//    public long registerUser(int identity, String password, String name, String gender){
//        User user = new User(identity, password, name, gender);
//        usersMapper.registerUser(user);
//        return user.getUser_id();
//    }

}

package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.AsyncTask;
import com.example.projecthelper.util.FileUtil;
import com.example.projecthelper.util.FormatUtil;
import com.example.projecthelper.util.IdentityCode;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import com.example.projecthelper.util.Wrappers.ObjectCountWrapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogoutBLKService logoutBLKService;

    @Autowired
    private AsyncService asyncService;

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    /**
     * 注册用户
     * @param user 用户
     * @return JWT
     */
    public String registerUser(User user){

        boolean strongPass = FormatUtil.match(user.getPassword(), FormatUtil.strongPasswordPredicate());
        boolean validUserId = FormatUtil.match(String.valueOf(user.getUserId()), FormatUtil.userIdPredicate());
        boolean validIdentity = FormatUtil.match(user.getIdentity(), FormatUtil.inCollection(IdentityCode.codeList()));
        if(!strongPass || !validIdentity || !validUserId)
            throw new InvalidFormException("密码太弱或身份不合法或id不合法");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean validName = user.getName() != null;
        boolean validGender = FormatUtil.match(user.getGender(), FormatUtil.genderPredicate());
        if(!validName || !validGender)
            throw new InvalidFormException("名字不合法或性别不合法");
        try {

            usersMapper.registerUser(user);
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new InvalidFormException("信息不完整或id已经存在");
        }
        System.out.println(user);
        return JWTUtil.createJWT(String.valueOf(user.getUserId()), String.valueOf(user.getIdentity()));
    }

    //NOTE: 这个方法只给adm使用
    public void registerUserByAdm(ObjectCountWrapper<User> multiUsers, KeyValueWrapper<String, String> userPass){
        //FUNC:该功能需要密码验证
        authenticate(userPass);

        User user = multiUsers.getObj();
        if(user == null || user.getIdentity() < 1 || user.getIdentity() > 3){
            throw new InvalidFormException("提供的用户不合法");
        }
        long userId = user.getUserId();
        boolean strongPass = FormatUtil.match(user.getPassword(), FormatUtil.strongPasswordPredicate());
        boolean validUserId = FormatUtil.match(String.valueOf(user.getUserId()), FormatUtil.userIdPredicate());
        boolean validIdentity = FormatUtil.match(user.getIdentity(), FormatUtil.inCollection(IdentityCode.codeList())) && user.getIdentity() > 0 && user.getIdentity() < 4;
        if(!strongPass || !validIdentity || !validUserId)
            throw new InvalidFormException("密码太弱或身份不合法或id不合法");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean validName = user.getName() != null;
        boolean validGender = FormatUtil.match(user.getGender(), FormatUtil.genderPredicate());
        if(!validName || !validGender)
            throw new InvalidFormException("名字不合法或性别不合法");

        try {
            List<User> users = Stream.generate(() -> multiUsers.getObj().clone()).limit(multiUsers.getCount()).toList();
            for(User usr: users){
                usr.setUserId(userId++);
//                System.err.println(usr);
            }
            usersMapper.registerUsers(users);
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new InvalidFormException("信息不完整或id已经存在");
        }
    }

    public void createMultipleUsersWithFile(MultipartFile file, KeyValueWrapper<String, String> userPass){
        authenticate(userPass);
        LocalDateTime p0 = LocalDateTime.now();
        System.err.println("p1: "+ Duration.between(p0, LocalDateTime.now()).toMillis() / (float)1000);;
        List<User> users = FileUtil.tableToUsersList(file);

        System.err.println("p2: "+ Duration.between(p0, LocalDateTime.now()).toMillis() / (float)1000);;
        users = users
            .stream()
            .filter(u -> FormatUtil.match(u.getPassword(), FormatUtil.strongPasswordPredicate()))
            .filter(u -> FormatUtil.match(String.valueOf(u.getUserId()), FormatUtil.userIdPredicate()))
            .filter(u -> FormatUtil.match(u.getIdentity(), FormatUtil.inCollection(IdentityCode.codeList())) && u.getIdentity() > 0 && u.getIdentity() < 4)
            .filter(u -> u.getName() != null)
            .filter(u -> FormatUtil.match(u.getGender(), FormatUtil.genderPredicate()))
            .toList();

        System.err.println("p3: "+ Duration.between(p0, LocalDateTime.now()).toMillis() / (float)1000);;
        System.err.println("p4: "+ Duration.between(p0, LocalDateTime.now()).toMillis() / (float)1000);
        try{
            if (users.size() < 500){
                usersMapper.registerUsers(users);
            }
            else {
                System.err.println(users.size());
                List<AsyncTask> ats = new ArrayList<>();
                int listSize = users.size() / 20;
                for (int i = 0; i < 20; i++) {
                    final List<User> users1 = users.subList(i * listSize, Math.min((i+1)* listSize, users.size()));
                    ats.add(
                        () -> {
                            try{
                                System.err.println("hello1");
                                users1.forEach(
                                    u -> u.setPassword(passwordEncoder.encode(u.getPassword()))
                                );
                                usersMapper.registerUsers(users1);
                            }catch (Exception e){
                                System.err.println(e.getMessage());
                            }
                        }
                    );
                }
                System.err.println("hello2");
                asyncService.executeTasks(ats, 20);
            }
            System.err.println("p4");;
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new InvalidFormException("信息不完整或id已经存在");
        }

    }

    public void registerUserByTea(ObjectCountWrapper<User> multiUsers, KeyValueWrapper<String, String> userPass){
        //FUNC:该功能需要密码验证
        authenticate(userPass);

        User user = multiUsers.getObj();
        if(user == null || user.getIdentity() < 2 || user.getIdentity() > 3){
            throw new InvalidFormException("提供的用户不合法");
        }
        long userId = user.getUserId();
        boolean strongPass = FormatUtil.match(user.getPassword(), FormatUtil.strongPasswordPredicate());
        boolean validUserId = FormatUtil.match(String.valueOf(user.getUserId()), FormatUtil.userIdPredicate());
        boolean validIdentity = FormatUtil.match(user.getIdentity(), FormatUtil.inCollection(IdentityCode.codeList()));
        if(!strongPass || !validIdentity || !validUserId)
            throw new InvalidFormException("密码太弱或身份不合法或id不合法");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            List<User> users = Stream.generate(() -> multiUsers.getObj().clone()).limit(multiUsers.getCount()).toList();
            for(User usr: users){
                usr.setUserId(userId++);
//                System.err.println(usr);
            }
            usersMapper.registerUsers(users);
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new InvalidFormException("信息不完整或id已经存在");
        }
    }

    public void changePass(Long userId, String oriPass, String pass1, String pass2){
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(String.valueOf(userId), oriPass);

        authenticationManager.authenticate(authenticationToken);
        //上一步没有抛出异常说明认证成功
        boolean strongPass = FormatUtil.match(pass1, FormatUtil.strongPasswordPredicate());
        if(!Objects.equals(pass1, pass2) || !strongPass){
            throw new InvalidFormException("密码太弱两次密码不同");
        }
        usersMapper.changePass(userId, passwordEncoder.encode(pass1));

    }

    public void authenticate(KeyValueWrapper<String, String> userPass){
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userPass.getKey(), userPass.getValue());
        authenticationManager.authenticate(authenticationToken);
    }
    /**
     * 登录
     * @param userPass 用户密码
     * @return JWT
     */
    public String login(KeyValueWrapper<String, String> userPass){

        authenticate(userPass);

        //上一步没有抛出异常说明认证成功，我们向用户颁发jwt令牌
        //NOTE: 拉出黑名单
        logoutBLKService.removeFromBlacklist(userPass.getKey());
        //NOTE: 用userMapper获取含有userID与Identity的字段放入token中
        System.err.println(userPass.getKey()+" "+userPass.getValue());
        User user = null;
        user = usersMapper.findUserById(Long.parseLong(userPass.getKey()));
        System.err.println(user);
        String
            jwt = JWTUtil.createJWT(String.valueOf(user.getUserId()), String.valueOf(user.getIdentity()));
        System.err.println(JWTUtil.getExpiredTime(jwt));
        return jwt;


    }

    /**
     * 登出
     * @return JWT
     * @Improved 一个更可靠的办法是：将相关的token放进黑名单（存redis），然后调用的时候如果在黑名单则认证失败
     */
    public String logout(String userId){
        //NOTE: 加入黑名单
        logoutBLKService.addToBlacklist(userId);
        return null;
    }

    public void resetPassForMultiUsers(KeyValueWrapper<String, List<Long>> newPassAndIds, KeyValueWrapper<String, String> userPass){
        authenticate(userPass);
        boolean strongPass = FormatUtil.match(newPassAndIds.getKey(), FormatUtil.strongPasswordPredicate());
        if(!strongPass){
            throw new InvalidFormException("密码太弱");
        }
        usersMapper.resetPass(newPassAndIds.getValue(), passwordEncoder.encode(newPassAndIds.getKey()));
    }

    public void freezeMultiUsers(List<Long> ids, KeyValueWrapper<String, String> userPass){
        authenticate(userPass);
        usersMapper.freezeUsers(ids);

    }

    public void unfreezeMultiUsers(List<Long> ids, KeyValueWrapper<String, String> userPass){
        authenticate(userPass);
        usersMapper.unfreezeUsers(ids);
    }


}

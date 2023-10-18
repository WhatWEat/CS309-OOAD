package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.FormatUtil;
import com.example.projecthelper.util.IdentityCode;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    /**
     * 注册用户
     * @param user 用户
     * @return JWT
     */
    public String registerUser(User user){

        boolean strongPass = FormatUtil.match(user.getPassword(), FormatUtil.strongPasswordPredicate());
        boolean validIdentity = FormatUtil.match(user.getIdentity(), FormatUtil.inCollection(IdentityCode.codeList()));
        if(!strongPass || !validIdentity)
            throw new InvalidFormException("密码太弱或身份不合法");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            usersMapper.registerUser(user);
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.err.println("hello");
            throw new InvalidFormException("信息不完整");
        }
        System.out.println(user);
        return JWTUtil.createJWT(String.valueOf(user.getUserId()), String.valueOf(user.getIdentity()));
    }

    /**
     * 登录
     * @param userPass 用户密码
     * @return JWT
     */
    public String login(KeyValueWrapper userPass){

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userPass.getKey(), userPass.getValue());
        authenticationManager.authenticate(authenticationToken);

        //上一步没有抛出异常说明认证成功，我们向用户颁发jwt令牌
        //NOTE: 拉出黑名单
        logoutBLKService.removeFromBlacklist(userPass.getKey());
        //NOTE: 用userMapper获取含有userID与Identity的字段放入token中
        System.err.println(userPass.getKey()+" "+userPass.getValue());
        User user = null;
        user = usersMapper.findUserById(Integer.parseInt(userPass.getKey()));
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



}

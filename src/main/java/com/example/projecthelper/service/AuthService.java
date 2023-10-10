package com.example.projecthelper.service;

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

    //TODO: 判断是否登录以及identity,是的话返回JWT，否则返回null
//    public String checkLoginAndIdentity(){
//        try {
//            //TODO:获取用户的用户名和密码，Authenticate进行用户认证
//            String userId = "", password = "";
//            // 创建一个认证令牌
//
////            UsernamePasswordAuthenticationToken authenticationToken =
////                new UsernamePasswordAuthenticationToken(userId, password);
////
////            // 使用AuthenticationManager来验证这个令牌
////            Authentication authentication = authenticationManager.authenticate(authenticationToken);
////            if(Objects.isNull(authentication)) throw new AuthenticationException("authentication is null") {
////                @Override
////                public String getMessage() {
////                    return super.getMessage();
////                }
////            };
//
//            // 如果上面的方法没有抛出异常，那么认证就是成功的
//            // 在此处，您可以继续您的登录逻辑，例如生成JWT令牌等
//            //TODO:用Mapper获取该用户权限
////            int identityCode = IdentityCode.convertStringToInt(usersMapper.findUserById(Long.parseLong(userId)).getIdentity()).getValue();
//            int identityCode = 0;
//            //TODO:如果认证通过，生成JWT
//            return JWTUtil.createJWT(userId, String.valueOf(identityCode));
//
//            //TODO:将完整的用户信息存入redis
//        } catch (AuthenticationException e) {
//            // 这里处理认证失败的情况，如返回一个错误响应
//            //TODO:如果认证没通过，给出提示
//            return null;
//        }
//    }

    /**
     * 注册用户
     * @param user 用户
     * @return JWT
     */
    public String registerUser(User user){
        boolean strongPass = FormatUtil.match(user.getPassword(), FormatUtil.strongPasswordPredicate());
        boolean validIdentity = FormatUtil.match(user.getIdentity(), FormatUtil.inCollection(IdentityCode.codeList()));
        if(!strongPass || !validIdentity)
            return null;
        usersMapper.registerUser(user);
        return JWTUtil.createJWT(String.valueOf(user.getUser_id()), String.valueOf(user.getIdentity()));
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
        //TODO: 用userMapper获取含有userID与Identity的字段放入token中
        User user = usersMapper.findUserById(Integer.parseInt(userPass.getKey()));

        return JWTUtil.createJWT(String.valueOf(user.getUser_id()), String.valueOf(user.getIdentity()));


    }

    /**
     * 登出
     * @return JWT
     * @Improved 一个更可靠的办法是：将相关的token放进黑名单（存redis），然后调用的时候如果在黑名单则认证失败
     */
    public String logout(){
        return null;
    }



}

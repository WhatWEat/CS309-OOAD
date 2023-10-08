package com.example.projecthelper.service;

import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.IdentityCode;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.ResponseResult;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    //TODO: 判断是否登录以及identity,是的话返回JWT，否则返回null
    public String checkLoginAndIdentity(){
        try {
            //TODO:获取用户的用户名和密码，Authenticate进行用户认证
            String userId = "", password = "";
            // 创建一个认证令牌

//            UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(userId, password);
//
//            // 使用AuthenticationManager来验证这个令牌
//            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//            if(Objects.isNull(authentication)) throw new AuthenticationException("authentication is null") {
//                @Override
//                public String getMessage() {
//                    return super.getMessage();
//                }
//            };

            // 如果上面的方法没有抛出异常，那么认证就是成功的
            // 在此处，您可以继续您的登录逻辑，例如生成JWT令牌等
            //TODO:用Mapper获取该用户权限
//            int identityCode = IdentityCode.convertStringToInt(usersMapper.findUserById(Long.parseLong(userId)).getIdentity()).getValue();
            int identityCode = 0;
            //TODO:如果认证通过，生成JWT
            return JWTUtil.createJWT(userId, String.valueOf(identityCode));

            //TODO:将完整的用户信息存入redis
        } catch (AuthenticationException e) {
            // 这里处理认证失败的情况，如返回一个错误响应
            //TODO:如果认证没通过，给出提示
            return null;
        }
    }

    //TODO: 判断身份
    public IdentityCode checkIdentity(){
        return IdentityCode.ADMINISTRATOR;
    }




    //TODO：完成对是否login的判断，并返回一个User对象

    //TODO:autowired获取Mapper,用于查看权限


    //TODO:autowired获取RedisCache


    //TODO:verifyLoginState返回一个User对象，如果JWT认证通过，会将userId，identityCode放在相应的位置
    public boolean verifyLoginState(String token){
        try{
            return true;
        }catch (Exception e){
            return false;
        }
    }
    //TODO:获取user，返回ResponseResult

}

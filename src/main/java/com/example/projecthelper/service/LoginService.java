package com.example.projecthelper.service;

import com.example.projecthelper.util.security.ResponseResult;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    //TODO:autowired获取RedisCache

    //TODO:获取user，返回ResponseResult
    public ResponseResult<?> login(){
        //TODO:获取用户的用户名和密码，Authenticate进行用户认证
        String username = "", password = "";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //TODO:如果认证没通过，给出提示
        if(Objects.isNull(authentication)){
            //TODO:throw...
        }
        //TODO:如果认证通过，用userid生成JWT

        //TODO:将完整的用户信息存入redis

        return null;
    }
}

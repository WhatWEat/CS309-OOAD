package com.example.projecthelper.util.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    //TODO：设置一个mapper
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //TODO: 查询对应的用户信息
        //TODO：如果没有查询到，则抛出一个异常
        //TODO：将数据封装成UserDetails返回
        return null;
    }
}

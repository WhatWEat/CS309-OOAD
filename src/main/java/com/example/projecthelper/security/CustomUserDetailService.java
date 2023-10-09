package com.example.projecthelper.security;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 用于定义从数据库查询用户信息的行为
 */
@Service
public class CustomUserDetailService implements UserDetailsService {


    @Autowired
    private UsersMapper usersMapper;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        int userId = Integer.parseInt(username);
        User user = usersMapper.findUserById(userId);

        if (user == null) {
            LogUtil.log("User not found with userId: " + userId, LogUtil.WARN);
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }

        // 将您的User对象转换为Spring Security的UserDetails对象
        return new CustomUserDetails(user);
    }

}

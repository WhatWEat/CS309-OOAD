package com.example.projecthelper.security;

import com.example.projecthelper.entity.User;
import com.example.projecthelper.util.IdentityCode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * 用于将User对象最常用的信息包装成UserDetails
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String password;
    private final int identity;

    // 根据您的需求添加其他字段

    public CustomUserDetails(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.identity = user.getIdentity();
    }


    // 实现 UserDetails 接口的方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 转换identity为一个GrantedAuthority并添加到权限列表中
        authorities.add(new SimpleGrantedAuthority(IdentityCode.getICByCode(identity).name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(userId); // 如果您想使用身份作为用户名
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


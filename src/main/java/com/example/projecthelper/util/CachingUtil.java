package com.example.projecthelper.util;

import com.example.projecthelper.entity.User;
import lombok.Getter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@Getter
public class CachingUtil {
    private final CachingUtil cu = new CachingUtil();

    @Cacheable(key = "#Id", value = "UserById")
    public void getUserById(String Id){
        //TODO:向数据库查询得到USER对象，然后将返回值改为User
    }

    @CacheEvict(key = "#Id", value = "UserById")
    public void deleteUserById(String Id){
        //TODO:向数据库删除对应的user
    }

    @CachePut(value = "user", key = "#user")
    public void updateUser(String Id) {
        //TODO:向数据库更新对应的user
    }
}

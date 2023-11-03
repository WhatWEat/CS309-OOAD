package com.example.projecthelper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogoutBLKService {
    private static final String BLACKLIST_KEY = "__LOGOUT__BLACKLIST__";

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 添加ID到黑名单
    public void addToBlacklist(String id) {
        redisTemplate.opsForSet().add(BLACKLIST_KEY, id);
    }

    // 从黑名单中删除ID
    public void removeFromBlacklist(String id) {
        redisTemplate.opsForSet().remove(BLACKLIST_KEY, id);
    }

    // 检查ID是否在黑名单中
    public boolean isBlacklisted(String id) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(BLACKLIST_KEY, id));
    }
}

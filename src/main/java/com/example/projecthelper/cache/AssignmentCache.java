package com.example.projecthelper.cache;

import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.NoticeMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service

public class AssignmentCache {
    @Autowired
    private NoticeMapper noticeMapper; // 假设你有一个与数据库交互的Repository

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String NOTICE_CACHE_KEY = "Notices";

//    public List<Notice> getNotices() {
//        // 尝试从缓存中获取数据
//        List<Notice> notices = (List<Notice>) redisTemplate.opsForValue().get(NOTICE_CACHE_KEY);
//        if (notices == null) {
//            // 缓存中没有，从数据库中加载
//            System.err.println("从数据库中加载数据");
//            notices = noticeRepository.findAll();
//            // 将数据存入缓存
//            redisTemplate.opsForValue().set(NOTICE_CACHE_KEY, notices);
//        }
//        return notices;
//    }
//
//    public void updateNotice(Notice notice) {
//        // 更新数据库
//        noticeMapper.save(notice);
//        // 更新缓存
//        List<Notice> notices = noticeRepository.findAll();
//        redisTemplate.opsForValue().set(NOTICE_CACHE_KEY, notices);
//    }
}

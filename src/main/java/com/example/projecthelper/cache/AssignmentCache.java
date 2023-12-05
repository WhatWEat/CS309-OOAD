package com.example.projecthelper.cache;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.AssignmentMapper;
import com.example.projecthelper.mapper.NoticeMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service

public class AssignmentCache {
    @Autowired
    private AssignmentMapper assignmentMapper; // 假设你有一个与数据库交互的Repository

    @Autowired
    private RedisTemplate<String, Assignment> redisTemplate;

    private final ConcurrentHashMap<Long, Lock> redisLock = new ConcurrentHashMap<>();

    private static final String ASS_CACHE_KEY = "__ASSIGNMENT__";

    public List<Assignment> getAssignmentsInProj(Long pjId, int limit, int offset) {
        // 尝试从缓存中获取数据
        String key = ASS_CACHE_KEY+pjId;
        Long listSize = redisTemplate.opsForList().size(key);
        if(listSize != null && listSize != 0){
            List<Assignment> assignments = redisTemplate.opsForList().range(key, offset, Math.min(offset+limit, listSize-1));
            if (assignments == null) {
                System.err.println("assignments == null");
                return null;
            }
            return assignments;
        }
        else {
            // 缓存中没有，从数据库中加载
            System.err.println("从数据库中加载数据");
            List<Assignment> assignments = reloadData(pjId);
            return assignments.stream().limit(limit).toList();
        }
    }

    @Async
    public void deleteAss(Long pjId, Long assId) {
        System.err.println("向缓存中更新数据");
        assignmentMapper.deleteAss(assId);
        reloadData(pjId);
    }

    private List<Assignment> reloadData(Long pjId){
        if(!redisLock.containsKey(pjId)){
            redisLock.put(pjId, new ReentrantLock());
        }
        Lock lock = redisLock.get(pjId);
        lock.lock();
        String key = ASS_CACHE_KEY+pjId;
        List<Assignment> assignments = assignmentMapper.getAssByProjWithoutLimit(pjId);
        // 将数据存入缓存
        redisTemplate.delete(key);
        for(Assignment ass: assignments){
            redisTemplate.opsForList().rightPush(key, ass);
        }
        return assignments;
    }
//    public void deleteAss()

    public void createAss(Assignment assignment){
        assignmentMapper.createAss(assignment);
        String key = ASS_CACHE_KEY+assignment.getProjectId();
        redisTemplate.opsForList().rightPush(key, assignment);
    }
}

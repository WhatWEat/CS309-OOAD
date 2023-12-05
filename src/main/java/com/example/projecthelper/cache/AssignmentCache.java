package com.example.projecthelper.cache;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.AssignmentMapper;
import com.example.projecthelper.mapper.NoticeMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.*;
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
        Long zsetSize = redisTemplate.opsForZSet().size(key);
        if (zsetSize != null && zsetSize != 0) {
            Set<TypedTuple<Assignment>> assignments =
                redisTemplate.opsForZSet().
                    rangeWithScores(key, offset, Math.min(offset+limit-1, zsetSize-1));
            if (assignments == null) {
                System.err.println("assignments == null");
                return null;
            }

            return assignments.stream().map(TypedTuple::getValue).toList();
        } else {
            System.err.println("从数据库中加载数据");
            List<Assignment> assignments = reloadData(pjId);
            return assignments.stream().limit(limit).toList();
        }
    }

    @Async
    public void deleteAss(Long pjId, Long assId) {
        String key = ASS_CACHE_KEY+pjId;
        assignmentMapper.deleteAss(assId);
        redisTemplate.opsForZSet().removeRange(key, assId, assId);
    }

    private List<Assignment> reloadData(Long pjId){
        if(!redisLock.containsKey(pjId)){
            redisLock.put(pjId, new ReentrantLock());
        }
        Lock lock = redisLock.get(pjId);
        lock.lock();

        String key = ASS_CACHE_KEY+pjId;
        List<Assignment> assignments = assignmentMapper.getAssByProjWithoutLimit(pjId);
        // 使用批量操作更新缓存
        redisTemplate.delete(key);
        Set<TypedTuple<Assignment>> assignmentWithScores = new HashSet<>();
        for (Assignment ass : assignments) {
            double score = -ass.getAssignmentId();
            assignmentWithScores.add(new DefaultTypedTuple<>(ass, score));
        }
        redisTemplate.opsForZSet().add(key, assignmentWithScores);

        lock.unlock();
        return assignments;
    }

    public void createAss(Assignment assignment){
        assignmentMapper.createAss(assignment);
        String key = ASS_CACHE_KEY+assignment.getProjectId();
        Set<TypedTuple<Assignment>> assignmentWithScores = new HashSet<>();
        assignmentWithScores.add(new DefaultTypedTuple<>(assignment, -(double)assignment.getAssignmentId()));
        redisTemplate.opsForZSet().add(key, assignmentWithScores);
    }

}

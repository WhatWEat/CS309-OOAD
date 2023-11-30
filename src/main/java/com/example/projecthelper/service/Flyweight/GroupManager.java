package com.example.projecthelper.service.Flyweight;

import com.example.projecthelper.Exceptions.OverdueException;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.mapper.GroupMapper;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GroupManager {

    private GroupMapper groupMapper;

    public GroupManager(GroupMapper groupMapper){
        this.groupMapper = groupMapper;
    }


    public synchronized void stuJoinGpSync(Long gpId, Long stuId, boolean needApp){
        Group gp = groupMapper.findGroupById(gpId);
        int cnt = groupMapper.findCntOfStuInGroup(gpId);
        if(gp.getDeadline().isBefore(LocalDateTime.now()))
            throw new OverdueException("超过组队的截止时间", gp.getDeadline(), LocalDateTime.now());
        if(cnt == 0){
            groupMapper.stuJoinGroup(stuId, gpId);
            try{
                groupMapper.updateLeader(stuId, gpId);
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }else if(gp.getMaxsize() == cnt) {
            throw new AccessDeniedException("小组已满");
        }else if(needApp){
            throw new AccessDeniedException("小组不是空组，请向小组长申请");
        }else {
            groupMapper.stuJoinGroup(stuId, gpId);
        }
    }


}

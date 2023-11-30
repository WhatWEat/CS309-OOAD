package com.example.projecthelper.service.Flyweight;

import com.example.projecthelper.Exceptions.OverdueException;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.NoticeMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GroupManager {

    private GroupMapper groupMapper;
    private NoticeMapper noticeMapper;

    public GroupManager(GroupMapper groupMapper, NoticeMapper noticeMapper){
        this.groupMapper = groupMapper;
        this.noticeMapper = noticeMapper;
    }


    public synchronized void stuJoinGpSync(Long gpId, Long stuId, boolean needApp, Long noticeId){
        Group gp = groupMapper.findGroupById(gpId);
        Notice nt = noticeMapper.findNoticeById(noticeId);
        int cnt = groupMapper.findCntOfStuInGroup(gpId);
        if(gp.getDeadline().isBefore(LocalDateTime.now()))
            throw new OverdueException("超过组队的截止时间", gp.getDeadline(), LocalDateTime.now());
        if(cnt == 0){
            Set<Long> undecided = new HashSet<>(noticeMapper.findUndecidedAppInvOfStuAndProj(stuId, gp.getProjectId()));
            undecided.remove(noticeId);
            if(!undecided.isEmpty())
                noticeMapper.updateNoticeStatusBySet(undecided, Notice.Status.EXPIRED.getValue());

            groupMapper.stuJoinGroup(stuId, gpId);
            try{
                groupMapper.updateLeader(stuId, gpId);
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        } else if(needApp){
            throw new AccessDeniedException("小组不是空组，请向小组长申请");
        } else {
            //FUNC: 针对invitation或者application
            if(gp.getMaxsize() == cnt) {
                nt.setStatus(Notice.Status.EXPIRED.getValue());
                noticeMapper.updateNoticeStatus(nt);
                throw new AccessDeniedException("小组已满");
            }

            if(nt.getStatus() == Notice.Status.EXPIRED.getValue())
                throw new AccessDeniedException("该公告已经过期");

            Set<Long> undecided = new HashSet<>(noticeMapper.findUndecidedAppInvOfStuAndProj(stuId, gp.getProjectId()));
            undecided.remove(noticeId);
            System.err.println(undecided+" \n"+noticeId);
            if(!undecided.isEmpty())
                noticeMapper.updateNoticeStatusBySet(undecided, Notice.Status.EXPIRED.getValue());
            noticeMapper.updateNoticeStatusBySet(Collections.singleton(noticeId), Notice.Status.AGREE.getValue());
            groupMapper.stuJoinGroup(stuId, gpId);
        }
    }


}

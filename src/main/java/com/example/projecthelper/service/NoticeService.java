package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import com.example.projecthelper.mapper.UsersMapper;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.aspectj.weaver.ast.Not;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private GroupMapper groupMapper;

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private Set<Long> toStu(Notice notice){
        Set<Long> toIds = null;
        if(notice.getToAll()){
            //FUNC:在这个proj的学生都能看到
            toIds = new HashSet<>(projectMapper.findStuIdsByProject(notice.getProjectId()));
        }
        else {
            List<Long> ids = notice.getStuView();
            //FUNC: 筛选掉identity不等于3的
            toIds = usersMapper
                .findUsersById(ids)
                .stream()
                .filter(user -> Objects.equals(user.getIdentity(), 3))
                .map(User::getUserId).collect(Collectors.toSet());
        }

        return toIds;
    }


    public List<Notice> getNoticesByTeacher(Long userId, Long projId, Long page, Long pageSize){
        if(projId == -1)
            return noticeMapper.findNoticeOfTea(userId, pageSize, page * pageSize);
        Long teaOfProj = projectMapper.findTeacherByProject(projId);
        if(!Objects.equals(teaOfProj, userId)){
            throw new AccessDeniedException("无权访问该project");
        }
        return noticeMapper.findNoticeOfTeaAndProj(userId, projId, pageSize, page * pageSize);
    }

    public List<Notice> getNoticesByTa(Long userId, Long projId, Long page, Long pageSize){
        if(projId == -1)
            return noticeMapper.findNoticeOfTa(userId, pageSize, page * pageSize);
        Long teaOfProj = projectMapper.findTeacherByProject(projId);
        if(!Objects.equals(teaOfProj, userId)){
            throw new AccessDeniedException("无权访问该project");
        }
        return noticeMapper.findNoticeOfTeaAndProj(userId, projId, pageSize, page * pageSize);
    }


    public List<Notice> getNoticesByStudent(Long userId, Long projId, Long page, Long pageSize){
        Long checker = projectMapper.checkStuInProj(userId, projId);
        if(checker == null && projId != -1){
            throw new AccessDeniedException("无权访问该project");
        }
        if(projId == -1)
            return noticeMapper.findNoticeOfStu(userId, pageSize, page * pageSize);
        return noticeMapper.findNoticeOfStuAndProj(userId, projId, pageSize, page * pageSize);
    }

    //PROC：get Notice --> get creator of Project --> compare the id in JWT --> set creatorId of Notice --> insert
    public void postNotice(Notice notice, Long creatorId, Predicate<Long> accessProject){
        if (accessProject.test(notice.getProjectId())) {
            try {
                notice.setCreatorId(creatorId);
                notice.setCreateTime(LocalDateTime.now());
                noticeMapper.createNotice(notice);
                System.err.println(notice.getNoticeId());
                Set<Long> toIds = toStu(notice);
                if(!toIds.isEmpty())
                    noticeMapper.insertStuView(toIds, notice.getNoticeId());
            } catch (Exception e) {
                throw new InvalidFormException("title、content、creatorId、projectId均不为空，title长度上限为200，content为5000");
            }
        }
        else {
            throw new AccessDeniedException("您没有权限发布该公告");
        }
    }

    public static final int APPLICATION = 0;
    public static final int INVITATION = 1;

    public String noticeSubject(Long fromId, Long gpId, int type){
        StringBuilder sb = new StringBuilder();
        User user = usersMapper.findUserById(fromId);
        Group gp =  groupMapper.findGroupById(gpId);
        switch (type){
            case 0:
                sb.append("From: <").append(fromId).append(">")
                    .append("Group: <").append(gpId).append(">")
                    .append("Type: <APPLICATION>")
                    .append("Status: <Undetermined>")
                    .append("Description: <I'm ").append(user.getName()).append(" and I want to join your group. >");
                break;
            case 1:
                sb.append("From: <").append(fromId).append(">")
                    .append("Group: <").append(gpId).append(">")
                    .append("Type: <INVITATION>")
                    .append("Status: <Undetermined>")
                    .append("Description: <I'm ").append(user.getName()).append(" and I want you to join group \"").append(gp.getGroupName()).append("\". >");
                break;
        }
        return null;
    }



    public Notice recruitNotice(Long fromId, Long pjId, Long gpId){
        Notice ntc = new Notice();
        ntc.setProjectId(pjId);
        ntc.setTitle("Invitation to Join Group");
        ntc.setContent(noticeSubject(fromId, gpId, INVITATION));
        return ntc;
    }
    public void createRecruitmentNotice(Long fromId, List<Long> toIds, Long pjId, Long gpId){
        try {
            Notice notice = recruitNotice(fromId, pjId, gpId);
            notice.setCreatorId(fromId);
            notice.setCreateTime(LocalDateTime.now());
            noticeMapper.createNotice(notice);
            System.err.println(notice.getNoticeId());
            noticeMapper.insertStuView(new HashSet<>(toIds), notice.getNoticeId());
        } catch (Exception e) {
            throw new InvalidFormException("title、content、creatorId、projectId均不为空，title长度上限为200，content为5000");
        }
    }
    public Notice appNotice(Long fromId, Long pjId, Long gpId){
        Notice ntc = new Notice();
        ntc.setProjectId(pjId);
        ntc.setTitle("Application to Join Group");
        ntc.setContent(noticeSubject(fromId, gpId, APPLICATION));
        return ntc;
    }
    public void createApplicationNotice(Long fromId, Long toId, Long pjId, Long gpId){
        try {
            Notice notice = appNotice(fromId, pjId, gpId);
            notice.setCreatorId(fromId);
            notice.setCreateTime(LocalDateTime.now());
            noticeMapper.createNotice(notice);
            System.err.println(notice.getNoticeId());
            noticeMapper.stuViewNotice(notice.getNoticeId(), toId);
        } catch (Exception e) {
            throw new InvalidFormException("title、content、creatorId、projectId均不为空，title长度上限为200，content为5000");
        }
    }

    //PROC：get Notice --> get noticeId --> compare creatorId and id in JWT --> update
    public void modifyNoticeWithUser(Notice notice, Predicate<Long> accessNotice){
        try {
            if(!accessNotice.test(notice.getNoticeId()))
                throw new AccessDeniedException("您没有权限修改该公告");
            notice.setProjectId(noticeMapper.findNoticeById(notice.getNoticeId()).getProjectId());
            noticeMapper.updateNotice(notice);
            Set<Long> toIds = toStu(notice);
            noticeMapper.deleteStuViewNoticeByNotice(notice.getNoticeId());
            System.err.println("toIds"+toIds);
            if(!toIds.isEmpty())
                noticeMapper.insertStuView(toIds, notice.getNoticeId());
        } catch (Exception e) {
            throw new InvalidFormException("title or content is null");
        }
    }

    //PROC：get noticeId --> compare creatorId and id in JWT --> delete --> deleteStuView
    public void deleteNotice(Long noticeId, Predicate<Long> accessNotice){
        if (accessNotice.test(noticeId)) {
            noticeMapper.deleteNotice(noticeId);
            noticeMapper.deleteStuViewNoticeByNotice(noticeId);
        }
        else
            throw new AccessDeniedException("您没有权限删除该公告");
    }

    public Notice findNoticeById(Long noticeId){
        return noticeMapper.findNoticeById(noticeId);
    }

//    public void createNotice(String title,String content,Integer creatorId){
//        try {
//            noticeMapper.createNotice(title, content,creatorId);
//        } catch (PSQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public boolean stuViewNotice(long notice_id, long[] stu_id,long user_id){
        //多个学生看到通知
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            for (long stuId : stu_id) {
                try {
                    noticeMapper.stuViewNotice(notice_id, stuId);
                } catch (PSQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        }
        return false;
    }
    public boolean stu1ViewNotice(long notice_id, long stu_id,long user_id){
        //一个学生
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            try {
                noticeMapper.stuViewNotice(notice_id, stu_id);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
    public boolean deleteStuViewNotice(long notice_id, long stu_id,long user_id){
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            noticeMapper.deleteStuViewNoticeByStu(notice_id, stu_id);
            return true;
        }
        return false;
    }

}

package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.NoticeFactory.AbstractNoticeFactory;
import com.example.projecthelper.entity.NoticeFactory.ApplicationFactory;
import com.example.projecthelper.entity.NoticeFactory.InvitationFactory;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import java.util.ArrayList;
import java.util.Collections;
import org.aspectj.weaver.ast.Not;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    private Set<Long> toStu(Notice notice) {
        Set<Long> toIds = null;
        if (notice.getToAll()) {
            //FUNC:在这个proj的学生都能看到
            toIds = new HashSet<>(projectMapper.findStuIdsByProject(notice.getProjectId()));
        } else {
            List<Long> ids = notice.getStuView();
            //FUNC: 筛选掉identity不等于3的
            if(ids!= null && !ids.isEmpty())
                toIds = usersMapper
                    .findUsersById(ids)
                    .stream()
                    .filter(user -> Objects.equals(user.getIdentity(), 3))
                    .map(User::getUserId).collect(Collectors.toSet());
            else toIds = new HashSet<>();
        }

        return toIds;
    }

    public List<Notice> getNoticesByAdm(Long page, Long pageSize, String key) {
        if(key == null)
            key = "%";
        else
            key = "%"+key+"%";

        List<Notice> result = noticeMapper.findNoticeOfAdm(pageSize, page * pageSize, key);
        result.forEach(e -> {
            e.setStuView(noticeMapper.findStuOfNotice(e.getNoticeId()));
            e.setStuViewName(usersMapper.findUsernamesById(e.getStuView()));
        });
        return result;
    }

    public List<Notice> getNoticesByTeacher(Long userId, Long projId, Long page, Long pageSize, String key) {
        if(key == null)
            key = "%";
        else
            key = "%"+key+"%";
        List<Notice> result = null;
        if (projId == -1)
            result =  noticeMapper.findNoticeOfTea(userId, pageSize, page * pageSize, key);
        else
            result = noticeMapper.findNoticeOfTeaAndProj(userId, projId, pageSize, page * pageSize, key);
        result.forEach(e -> {
            if(Objects.equals(userId, projectMapper.findTeacherByProject(e.getProjectId())) && e.getType() == 0){
                e.setStuView(noticeMapper.findStuOfNotice(e.getNoticeId()));
                if(e.getStuView() != null && !e.getStuView().isEmpty())
                    e.setStuViewName(usersMapper.findUsernamesById(e.getStuView()));
                else
                    e.setStuViewName(new ArrayList<>());
            }
        });
        return result;
    }

    public List<Notice> getNoticesByTa(Long userId, Long projId, Long page, Long pageSize, String key) {
        if(key == null)
            key = "%";
        else
            key = "%"+key+"%";
        List<Notice> result = null;
        if (projId == -1)
            result =  noticeMapper.findNoticeOfTea(userId, pageSize, page * pageSize, key);
        else
            result = noticeMapper.findNoticeOfTeaAndProj(userId, projId, pageSize, page * pageSize, key);
        result.forEach(e -> {
            if(Objects.equals(userId, e.getCreatorId()) && e.getType() == 0){
                e.setStuView(noticeMapper.findStuOfNotice(e.getNoticeId()));
                e.setStuViewName(usersMapper.findUsernamesById(e.getStuView()));
            }
        });
        return result;

    }


    public List<Notice> getNoticesByStudent(Long userId, Long projId, Long page, Long pageSize, String key) {
        if(key == null)
            key = "%";
        else
            key = "%"+key+"%";
        Long checker = projectMapper.checkStuInProj(userId, projId);
        if (checker == null && projId != -1) {
            throw new AccessDeniedException("无权访问该project");
        }

        List<Notice> result = null;
        if (projId == -1)
            result =  noticeMapper.findNoticeOfTea(userId, pageSize, page * pageSize, key);
        else
            result = noticeMapper.findNoticeOfTeaAndProj(userId, projId, pageSize, page * pageSize, key);
        result.forEach(e -> {
            if(Objects.equals(userId, e.getCreatorId())){
                e.setStuView(noticeMapper.findStuOfNotice(e.getNoticeId()));
                e.setStuViewName(usersMapper.findUsernamesById(e.getStuView()));
            }
        });
        return result;

    }

    //PROC：get Notice --> get creator of Project --> compare the id in JWT --> set creatorId of Notice --> insert
    public void postNotice(Notice notice, Long creatorId, Predicate<Long> accessProject) {
        if (accessProject.test(notice.getProjectId())) {
            try {
                notice.setCreatorId(creatorId);
                notice.setCreateTime(LocalDateTime.now());
                noticeMapper.createNotice(notice);
                System.err.println(notice.getNoticeId());
                Set<Long> toIds = toStu(notice);
                if (!toIds.isEmpty())
                    noticeMapper.insertStuView(toIds, notice.getNoticeId());
            } catch (Exception e) {
                throw new InvalidFormException("title、content、creatorId、projectId均不为空，title长度上限为200，content为5000");
            }
        } else {
            throw new AccessDeniedException("您没有权限发布该公告");
        }
    }

    public static final int APPLICATION = 0;
    public static final int INVITATION = 1;
    public static final int REMOVE = 2;
    public static final int TRANSFER = 3;


    public String noticeSubject(Long fromId, Long gpId, int type) {
        StringBuilder sb = new StringBuilder();
        User user = usersMapper.findUserById(fromId);
        Group gp = groupMapper.findGroupById(gpId);
        switch (type) {
            case 0 -> sb.append("From: <").append(fromId).append(">")
                    .append("Group: <").append(gpId).append(">")
                    .append("Type: <APPLICATION>")
                    .append("Status: <Undetermined>")
                    .append("Description: <I'm ").append(user.getName()).append(" and I want to join your group. >");
            case 1 -> sb.append("From: <").append(fromId).append(">")
                    .append("Group: <").append(gpId).append(">")
                    .append("Type: <INVITATION>")
                    .append("Status: <Undetermined>")
                    .append("Description: <I'm ").append(user.getName()).append(" and I want you to join group \"").append(gp.getGroupName()).append("\". >");
            case 2 -> sb.append("From: <").append(fromId).append(">")
                    .append("Group: <").append(gpId).append(">")
                    .append("Type: <REMOVE>")
                    .append("Status: <Undetermined>")
                    .append("Description: <You have been removed from the group \"").append(gp.getGroupName()).append("\". >");
            case 3 -> sb.append("From: <").append(fromId).append(">")
                    .append("Group: <").append(gpId).append(">")
                    .append("Type: <Transfer>")
                    .append("Status: <Undetermined>")
                    .append("Description: <You have been the leader of the group \"").append(gp.getGroupName()).append("\". >");
        }
        return sb.toString();
    }


    public void createRecruitmentNotice(KeyValueWrapper<Long, Notice> gpId_notice, Long userId) {
        //FUNC: 确定userId在group中
        Group group = groupMapper.findGroupById(gpId_notice.getKey());
        if(group == null || !Objects.equals(
            groupMapper.findGroupOfStuInProject(userId, group.getProjectId()).getGroupId(),
            group.getGroupId())){
            throw new InvalidFormException("无效的groupId");
        }
        //PROC: 筛选stuIds
        Set<Long> stuIds = gpId_notice.getValue().getStuView().stream()
            .filter(
                e -> projectMapper.checkStuInProj(e, group.getProjectId()) != null
            )
            .collect(Collectors.toSet());
        stuIds.retainAll(
            groupMapper.findStuNotInGpOfAProj(group.getProjectId()).stream()
                .map(User::getUserId)
                .collect(Collectors.toSet())
        );
//        try {
            AbstractNoticeFactory anf = new InvitationFactory();
            Notice notice = gpId_notice.getValue();
            notice.setCreatorId(userId);
            notice.setGroupId(group.getGroupId());
            notice.setProjectId(group.getProjectId());

            notice = anf.createNotice(notice);
            for(Long stuId: stuIds){
                Notice previous = noticeMapper.getPreviousUndecidedNotice(userId, stuId, Notice.Type.RECRUITMENT.getValue());
                if(previous != null){
                    previous.setCreateTime(LocalDateTime.now());
                    noticeMapper.updateNoticeTime(previous);
                    continue;
                }
                noticeMapper.createNotice(notice);
                noticeMapper.insertStuView(Collections.singleton(stuId), notice.getNoticeId());
            }

//        } catch (Exception e) {
//            throw new InvalidFormException("title、content、creatorId、projectId均不为空，title长度上限为200，content为5000");
//        }
    }


    public void createApplicationNotice(KeyValueWrapper<Long, Notice> gpId_notice, Long userId) {
        //FUNC: 确定userId在group中
        Group group = groupMapper.findGroupById(gpId_notice.getKey());
        if(group == null ||
            groupMapper.findGroupOfStuInProject(userId, group.getProjectId()) != null
        ){
            throw new InvalidFormException("无效的groupId或者已经加入小组");
        }
        try {
            AbstractNoticeFactory anf = new ApplicationFactory();
            Notice notice = gpId_notice.getValue();
            notice.setCreatorId(userId);
            notice.setGroupId(group.getGroupId());
            notice.setProjectId(group.getProjectId());

            notice = anf.createNotice(notice);
            Notice previous = noticeMapper.getPreviousUndecidedNotice(userId, group.getLeaderId(), Notice.Type.RECRUITMENT.getValue());
            if(previous != null){
                previous.setCreateTime(LocalDateTime.now());
                noticeMapper.updateNoticeTime(previous);
            }
            else {
                noticeMapper.createNotice(notice);
                System.err.println(notice.getNoticeId());
                noticeMapper.stuViewNotice(notice.getNoticeId(), group.getLeaderId());
            }
        } catch (Exception e) {
            throw new InvalidFormException("title、content、creatorId、projectId均不为空，title长度上限为200，content为5000");
        }
    }

    public Notice removeNotice(Long fromId, Long pjId, Long gpId) {
        Notice ntc = new Notice();
        ntc.setProjectId(pjId);
        ntc.setTitle("Removed from Group");
        ntc.setContent(noticeSubject(fromId, gpId, REMOVE));
        return ntc;
    }

    public void createRemoveNotice(Long fromId, Long toId, Long pjId, Long gpId) {
        try {
            Notice notice = removeNotice(fromId, pjId, gpId);
            notice.setCreatorId(fromId);
            notice.setCreateTime(LocalDateTime.now());
            noticeMapper.createNotice(notice);
            System.err.println(notice.getNoticeId());
            noticeMapper.stuViewNotice(notice.getNoticeId(), toId);
        } catch (Exception e) {
            throw new InvalidFormException("title、content、creatorId、projectId均不为空，title长度上限为200，content为5000");
        }
    }

    public Notice transferNotice(Long fromId, Long pjId, Long gpId) {
        Notice ntc = new Notice();
        ntc.setProjectId(pjId);
        ntc.setTitle("Group Leader Transfer");
        ntc.setContent(noticeSubject(fromId, gpId, TRANSFER));
        return ntc;
    }

    public void createTransferNotice(Long fromId, Long toId, Long pjId, Long gpId) {
        try {
            Notice notice = transferNotice(fromId, pjId, gpId);
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
    public void modifyNoticeWithUser(Notice notice, Predicate<Long> accessNotice) {
        try {
            if (!accessNotice.test(notice.getNoticeId()))
                throw new AccessDeniedException("您没有权限修改该公告");
            notice.setProjectId(noticeMapper.findNoticeById(notice.getNoticeId()).getProjectId());
            noticeMapper.updateNotice(notice);
            Set<Long> toIds = toStu(notice);
            noticeMapper.deleteStuViewNoticeByNotice(notice.getNoticeId());
            System.err.println("toIds" + toIds);
            if (!toIds.isEmpty())
                noticeMapper.insertStuView(toIds, notice.getNoticeId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new InvalidFormException("title or content is null");
        }
    }

    //PROC：get noticeId --> compare creatorId and id in JWT --> delete --> deleteStuView
    public void deleteNotice(List<Long> noticeIds, Predicate<Long> accessNotice) {
        noticeIds.forEach(noticeId -> {
            if (accessNotice.test(noticeId)) {
                noticeMapper.deleteNotice(noticeId);
                noticeMapper.deleteStuViewNoticeByNotice(noticeId);
            }
        });

    }

    public Notice findNoticeById(Long noticeId) {
        return noticeMapper.findNoticeById(noticeId);
    }

//    public void createNotice(String title,String content,Integer creatorId){
//        try {
//            noticeMapper.createNotice(title, content,creatorId);
//        } catch (PSQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public boolean stuViewNotice(long notice_id, long[] stu_id, long user_id) {
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

    public boolean stu1ViewNotice(long notice_id, long stu_id, long user_id) {
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

    public boolean deleteStuViewNotice(long notice_id, long stu_id, long user_id) {
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            noticeMapper.deleteStuViewNoticeByStu(notice_id, stu_id);
            return true;
        }
        return false;
    }

}

package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.Exceptions.OverdueException;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.User;

import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.service.Flyweight.GroupManagerFactory;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import com.example.projecthelper.util.Wrappers.ObjectCountWrapper;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cglib.core.Local;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class GroupService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private NoticeService noticeService;

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    public List<User> getTaListOfProj(Long projId, Long userId){
        if(!Objects.equals(userId, projectMapper.findTeacherByProject(projId))){
            throw new AccessDeniedException("无权查看小组");
        }
        List<User> users = groupMapper.getTaListOfProj(projId);
        users.forEach(e -> {
            e.setPassword(null);
            e.setAvatarPath(null);
        });
        return users;
    }

    public List<User> getStuListOfProj(Long projId, Long userId, int page, int page_size){

        if(!Objects.equals(userId, projectMapper.findTeacherByProject(projId))){
            throw new AccessDeniedException("无权查看小组");
        }
        List<User> users = groupMapper.getStuListOfProj(projId, page_size, page * page_size);
        users.forEach(e -> {
            e.setPassword(null);
            e.setAvatarPath(null);
        });
        return users;
    }

    public List<Group> getBriefGroupsFromProj(Long projId, Long userId){

        List<Group> groups = groupMapper.getBriefGroupsFromProj(projId);
        groups.forEach(g -> {
            g.setMemCnt(groupMapper.findCntOfStuInGroup(g.getGroupId()));
            g.setMembers(groupMapper.getMembersFromGp(g.getGroupId()).stream().map(User::getName).toList());
            g.setMemberIds(
                groupMapper.getMembersFromGp(g.getGroupId()).stream().map(User::getUserId).toList()
            );
            if(groupMapper.checkStuInGroup(g.getGroupId(), userId) == null &&
                !Objects.equals(projectMapper.findTeacherByProject(g.getProjectId()), userId) &&
                projectMapper.checkTaInProj(g.getProjectId(), userId) == null
            )
                g.mask();
        });
        return groups;
    }

    public Group getGroupById(Long groupId, Long userId){
        Group gp = groupMapper.findGroupById(groupId);
        if(gp == null){
            throw new InvalidFormException("不合法的groupId");
        }
        gp.setMemCnt(groupMapper.findCntOfStuInGroup(gp.getGroupId()));
        gp.setMembers(groupMapper.getMembersFromGp(gp.getGroupId()).stream().map(User::getName).toList());
        gp.setMemberIds(
            groupMapper.getMembersFromGp(gp.getGroupId()).stream().map(User::getUserId).toList()
        );
        if(groupMapper.checkStuInGroup(groupId, userId) != null ||
            Objects.equals(projectMapper.findTeacherByProject(gp.getProjectId()), userId) ||
            projectMapper.checkTaInProj(gp.getProjectId(), userId) != null
        ){
            return gp;
        }
        else
            return gp.mask();
    }

    public Long createGroup(Group group, Long creatorId, Predicate<Long> accessProject){
        // FUNC: 给行projectId，调用者是否有权限使用
        if(!accessProject.test(group.getProjectId())){
            throw new AccessDeniedException("无权创建小组");
        }
        StringBuilder sb = new StringBuilder();
        if(group.getReportTime() != null && !group.getReportTime().isAfter(LocalDateTime.now()))
            sb.append("报告时间不能早于当前时间|");
        if(group.getDeadline() == null || !group.getDeadline().isAfter(LocalDateTime.now())){
            sb.append("组队截止时间应该晚于当前时间|");
        }
        if(group.getMaxsize() == null || group.getMaxsize() <= 0)
            sb.append("maxsize无意义|");
        User usr = usersMapper.findUserById(group.getInstructorId());
        if(usr == null || usr.getIdentity() > 2)
            sb.append("instructorId不存在或不合法|");
        Long leaderId = group.getLeaderId();
        if(leaderId != null && (
            projectMapper.checkStuInProj(leaderId, group.getProjectId()) == null ||
            groupMapper.findGroupOfStuInProject(leaderId, group.getProjectId()) != null)
        )
            sb.append("leaderId不合法或已经加入小组|");
        if(group.getMaxsize() == null)
            sb.append("maxsize不能为空|");
        if(group.getGroupName() == null)
            sb.append("groupName不能为空|");
        if(group.getProjectId() == null)
            sb.append("projectId不能为空|");
        if(group.getMemberIds() != null && group.getMemberIds().size() > group.getMaxsize())
            sb.append("人数超限|");
        if(!sb.isEmpty())
            throw new InvalidFormException(sb.toString());
        group.setCreatorId(creatorId);
        group.setTeamTime(LocalDateTime.now());
        System.err.println(group.getMemberIds());
        // 插入学生进组
        Set<Long> validIds;
        if(group.getMemberIds() == null)
            validIds = new HashSet<>();
        else
            validIds= group.getMemberIds().stream()
            .filter(e -> projectMapper.checkStuInProj(e, group.getProjectId()) != null)
            .filter(e -> groupMapper.findGroupOfStuInProject(e, group.getProjectId()) == null)
                .collect(Collectors.toSet());
        if(leaderId != null)
            validIds.add(leaderId);
        validIds = validIds.stream().limit(group.getMaxsize()).collect(Collectors.toSet());
        group.setMemberIds(new ArrayList<>(validIds));
        group.setTechnicalStack(group.getTechnicalStack() == null ? new ArrayList<>(): group.getTechnicalStack());
        try{
            groupMapper.createGroup(group);
            if(!validIds.isEmpty())
                groupMapper.insertStuIntoGps(validIds, group.getGroupId());
            System.err.println(validIds);
            System.err.println(group.getGroupId()); // 这个是对的
            return group.getGroupId();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return (long)-1;
    }
    public List<Long> createGroup(ObjectCountWrapper<Group> ocw, Long creatorId, Predicate<Long> accessProject){
        // FUNC: 给行projectId，调用者是否有权限使用
        if(!accessProject.test(ocw.getObj().getProjectId())){
            throw new AccessDeniedException("无权创建小组");
        }
        System.err.println(ocw.getObj().getProjectId());
        StringBuilder sb = new StringBuilder();
        if(ocw.getCount() <= 0)
            sb.append("请创建大于等于一个小组");
        if(ocw.getObj().getMaxsize() == null || ocw.getObj().getMaxsize() <= 0)
            sb.append("maxsize不合法|");
        if(ocw.getObj().getDeadline() == null ||  ocw.getObj().getDeadline().isBefore(LocalDateTime.now()))
            sb.append("截止时间不合法|");
        if(!sb.isEmpty())
            throw new InvalidFormException(sb.toString());
        List<Group> groups = Stream.generate(() -> ocw.getObj().clone()).limit(ocw.getCount()).toList();
        // 设置时间
        groups.forEach(g -> {
            System.err.println(g.getProjectId());
            g.setCreatorId(creatorId);
            g.setTeamTime(LocalDateTime.now());
        });
        groupMapper.createGroups(groups);
        return groups.stream().map(Group::getGroupId).toList();
    }

    public void updateAllGroupForTea(Group group, Long userId) {
        if (!Objects.equals(projectMapper.findTeacherByProject(group.getProjectId()), userId))
            throw new InvalidFormException("projectId 错误");

        try {
            if(group.getMaxsize() != null)
                groupMapper.updateMaxsizeForAllGroups(group);
            if(group.getReportTime() != null)
                if(group.getReportTime().isBefore(LocalDateTime.now()))
                    throw new InvalidFormException("报告时间不能早于当前时间");
                else
                    groupMapper.updateReportTimeForAllGroups(group);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void updateGroupForTea(Group group, Predicate<Long> accessGroup) {
        //此处存疑，前端能在group里装多少信息，是否能包括group的创建者（是否需要查询数据库获取创建者
        if (accessGroup.test(group.getGroupId())) {
            Long pjId = groupMapper.findPjIdOfGroup(group.getGroupId());
            StringBuilder sb = new StringBuilder();
            if(group.getInstructorId() == null || projectMapper.checkTaInProj(pjId, group.getInstructorId()) == null)
                sb.append("无效的instructorId|");
            if(group.getLeaderId() == null ||  groupMapper.checkStuInGroup(group.getGroupId(), group.getLeaderId()) == null)
                sb.append("无效的leaderId|");
            if(group.getMaxsize() == null || group.getMaxsize() < findMemberOfGroup(group.getGroupId()))
                sb.append("maxsize小于现在的人数|");
            if(group.getReportTime() == null || group.getReportTime().isBefore(LocalDateTime.now()))
                sb.append("报告时间应该晚于现在");
            if(group.getDeadline() == null || group.getDeadline().isBefore(LocalDateTime.now()))
                sb.append("组队截止日期应该晚于现在");
            if(!sb.isEmpty())
                throw new InvalidFormException(sb.toString());
            group.setTechnicalStack(group.getTechnicalStack() != null ? group.getTechnicalStack(): new ArrayList<>());
            Set<Long> validIds;
            if(group.getMemberIds() == null)
                validIds = new HashSet<>();
            else
                validIds= group.getMemberIds().stream()
                    .filter(e -> projectMapper.checkStuInProj(e, group.getProjectId()) != null)
                    .filter(e -> groupMapper.findGroupOfStuInProject(e, group.getProjectId()) == null)
                    .collect(Collectors.toSet());
            validIds.add(group.getLeaderId());
            validIds = validIds.stream().limit(group.getMaxsize()).collect(Collectors.toSet());
            groupMapper.deleteStuInGroup(group.getGroupId());
            groupMapper.insertStuIntoGps(validIds, group.getGroupId());
            try {
                groupMapper.updateGroupForTea(group);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else
            throw new AccessDeniedException("无权修改小组信息");
    }

    public void deleteGroupForTea(Long groupId, Long userId){
        if(!Objects.equals(userId, groupMapper.findCreatorByGroup(groupId))){
            throw new AccessDeniedException("无权删除小组");
        }

        groupMapper.deleteGroup(groupId);
        groupMapper.deleteStuInGroup(groupId);

    }



    public void ackInvitation(Long noticeId, Long stuId){
        Notice notice = noticeService.findNoticeById(noticeId);
        Set<Long> views = new HashSet<>(noticeMapper.findStuOfNotice(noticeId));
        if(!views.contains(stuId))
            throw new AccessDeniedException("无权访问该notice");
        if(notice == null)
            throw new InvalidFormException("无效的noticeid");
        if(notice.getType() != Notice.Type.RECRUITMENT.getValue()){
            throw new AccessDeniedException("该notice不是一个请求");
        }
        GroupManagerFactory.getInstance().
            getGroupManager(notice.getGroupId(), groupMapper, noticeMapper).
            stuJoinGpSync(notice.getGroupId(), stuId, false, noticeId);
    }

    public void nakInvitationOrApplication(Long noticeId, Long stuId){
        Notice notice = noticeService.findNoticeById(noticeId);
        Set<Long> views = new HashSet<>(noticeMapper.findStuOfNotice(noticeId));
        if(!views.contains(stuId))
            throw new AccessDeniedException("无权访问该notice");
        if(notice == null)
            throw new InvalidFormException("无效的noticeid");
        if(notice.getType() == Notice.Type.NORMAL.getValue()){
            throw new AccessDeniedException("该notice不是一个请求");
        }
        notice.setStatus(Notice.Status.REJECT.getValue());
        noticeMapper.updateNoticeStatus(notice);
    }

    public void ackApplication(Long noticeId, Long stuId){
        Notice notice = noticeService.findNoticeById(noticeId);
        Set<Long> views = new HashSet<>(noticeMapper.findStuOfNotice(noticeId));
        System.err.println(views+" "+stuId);
        if(!views.contains(stuId))
            throw new AccessDeniedException("无权访问该notice");
        if(notice == null)
            throw new InvalidFormException("无效的noticeid");
        if(notice.getType() != Notice.Type.APPLICATION.getValue()){
            throw new AccessDeniedException("该notice不是一个请求");
        }
        GroupManagerFactory.getInstance().
            getGroupManager(notice.getGroupId(), groupMapper, noticeMapper).
            stuJoinGpSync(notice.getGroupId(), notice.getCreatorId(), false, noticeId);

    }


//    public synchronized void stuJoinGpSync(Long gpId, Long stuId, boolean needApp){
//        Group gp = groupMapper.findGroupById(gpId);
//        int cnt = groupMapper.findCntOfStuInGroup(gpId);
//        if(gp.getDeadline().isAfter(LocalDateTime.now()))
//            throw new OverdueException("超过组队的截止时间", gp.getDeadline(), LocalDateTime.now());
//        if(cnt == 0){
//            groupMapper.stuJoinGroup(stuId, gpId);
//            try{
//                groupMapper.updateLeader(stuId, gpId);
//            }catch (Exception e){
//                System.err.println(e.getMessage());
//            }
//        }else if(gp.getMaxsize() == cnt) {
//            throw new AccessDeniedException("小组已满");
//        }else if(needApp){
//            throw new AccessDeniedException("小组不是空组，请向小组长申请");
//        }else {
//            groupMapper.stuJoinGroup(stuId, gpId);
//        }
//    }

    public List<User> getStuNotInGroup(Long pjId, Long userId){
        if(projectMapper.checkStuInProj(userId, pjId) == null){
            throw new AccessDeniedException("你不在小组中");
        }
        return groupMapper.findStuNotInGpOfAProj(pjId).stream().map(User::mask).toList();
    }
    public void recruitMem(KeyValueWrapper<Long, Notice> gpId_notice, Long userId){
        noticeService.createRecruitmentNotice( gpId_notice,  userId);
    }

    public void applyToJoinGroup(KeyValueWrapper<Long, Notice> gpId_notice, Long userId){
        noticeService.createApplicationNotice(gpId_notice, userId);
    }

    public void removeMen(KeyValueWrapper<Long, Notice> gpId_notice, Long userId){
        noticeService.createRemoveNotice( gpId_notice,  userId);
    }

    public void transferLeader(KeyValueWrapper<Long, Notice> gpId_notice, Long userId){
        noticeService.createTransferNotice( gpId_notice,  userId);
    }

    public void joinGroup(Long groupId, Long stuId){
        //PROC: 检查小组存在 -> 检查学生是不是在group对应的proj中 -> 检查是否已经加入小组 -> 成功加入
        System.err.println(groupId);
        Group gp = groupMapper.findGroupById(groupId);
        if(gp == null){
            throw new InvalidFormException("小组不存在");
        }
        if(projectMapper.checkStuInProj(stuId, gp.getProjectId()) == null){
            throw new AccessDeniedException("无权加入小组");
        }
        if(groupMapper.findGroupIdOfUserInAProj(stuId, gp.getProjectId()) != null){
            // 在这个proj中学生加入了其他小组
            throw new AccessDeniedException("您已经在其他小组中");
        }
        GroupManagerFactory.getInstance().
            getGroupManager(gp.getGroupId(), groupMapper, noticeMapper).
            stuJoinGpSync(
                groupId, stuId, true, null
            );
    }

    public List<User> getGpMem(Long userId, Long pjId){
        Group gp =  groupMapper.findGroupOfStuInProject(userId, pjId);
        if(gp == null){
            System.err.println("here");
            return null;
        }
        else {
            List<User> result = groupMapper.getGpMem(gp.getGroupId());
            return result.stream().map(User::mask).toList();
        }
    }

    public void leaveGroup(Long pjId, Long stuId){
        Long groupId = groupMapper.findGroupIdOfUserInAProj(stuId, pjId);
        if(groupId == null)
            throw new InvalidFormException("您不在小组中");
        Group gp = groupMapper.findGroupById(groupId);
        if(Objects.equals(gp.getLeaderId(), stuId))
            throw new InvalidFormException("组长不能退出小组");
        groupMapper.stuLeaveGroup(stuId, groupId);
    }

    public void updateLeader(Long leaderId, Long groupId, Predicate<Long> accessProject){
        Group group = groupMapper.findGroupById(groupId);
        if (group == null){
            throw new InvalidFormException("小组不存在");
        }
        if(!accessProject.test(group.getProjectId())){
            throw new AccessDeniedException("无权修改小组信息");
        }
        if(projectMapper.checkStuInProj(leaderId, group.getProjectId()) == null){
            throw new AccessDeniedException("所选成员不在project中");
        }
        if(groupMapper.findGroupIdOfUserInAProj(leaderId, group.getProjectId()) == null ||
                !groupMapper.findGroupIdOfUserInAProj(leaderId, group.getProjectId()).equals(group.getGroupId())){
            throw new AccessDeniedException("所选成员不在小组中");
        }
        try {
            groupMapper.updateLeader(leaderId,groupId);
        } catch (Exception e) {
            throw new InvalidFormException("leaderId需要为long");
        }
    }

    public void updateVisibility(long userId, long groupId,Boolean[] visibility){
        Group group = groupMapper.findGroupById(groupId);
        if (group == null){
            throw new InvalidFormException("小组不存在");
        }
        if (userId != group.getInstructorId() && userId != group.getLeaderId()){
            throw new AccessDeniedException("无权修改小组信息");
        }
        try {
            groupMapper.updateVisibility(groupId, visibility);
        } catch (Exception e) {
            throw new InvalidFormException("leaderId需要为long");
        }
    }

    public void updateGroupForLeader(Group group, long user_id) {
        //此处存疑，前端能在group里装多少信息，是否能包括group的创建者（是否需要查询数据库获取创建者
        long leader_id;
        leader_id = groupMapper.findLeaderByGroup(group.getGroupId());
        if (leader_id == user_id) {
            try {
                groupMapper.updateGroupForLeader(group);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    public void updateGroupInstructor(long instructor_id, long group_id) {
//        try {
//            groupMapper.updateGroupInstructor(instructor_id, group_id);
//        } catch (PSQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public Group findGroupOfStuInProject(long stu_id, long project_id){
        return groupMapper.findGroupOfStuInProject(stu_id,project_id);
    }

    public List<Group> findUndermannedGroup(long project_id){
        return groupMapper.findUndermannedGroup(project_id);
    }

    public List<Group> findAllGroup(long project_id){
        return groupMapper.findAllGroup(project_id);
    }

    public int findMemberOfGroup(long group_id){
        return groupMapper.findMemberOfGroup(group_id);
    }

    public Long findCreatorByGroup(Long groupId){
        return groupMapper.findCreatorByGroup(groupId);
    }

}

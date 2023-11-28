package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.User;

import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.UsersMapper;
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
        if(leaderId == null ||
            projectMapper.checkStuInProj(leaderId, group.getProjectId()) == null ||
            groupMapper.findGroupOfStuInProject(leaderId, group.getProjectId()) != null)
            sb.append("leaderId不合法或已经加入小组|");
        if(group.getMaxsize() == null)
            sb.append("maxsize不能为空|");
        if(group.getGroupName() == null)
            sb.append("groupName不能为空|");
        if(group.getProjectId() == null)
            sb.append("projectId不能为空|");
        if(!sb.isEmpty())
            throw new InvalidFormException(sb.toString());
        group.setCreatorId(creatorId);
        group.setTeamTime(LocalDateTime.now());
        System.err.println(group.getMemberIds());
        // 插入学生进组
        Set<Long> validIds = group.getMemberIds().stream()
            .filter(e -> projectMapper.checkStuInProj(e, group.getProjectId()) != null)
            .filter(e -> groupMapper.findGroupOfStuInProject(e, group.getProjectId()) == null)
                .collect(Collectors.toSet());
        validIds.add(leaderId);
        validIds = validIds.stream().limit(group.getMaxsize()).collect(Collectors.toSet());
        group.setMemberIds(new ArrayList<>(validIds));
        group.setTechnicalStack(group.getTechnicalStack() == null ? new ArrayList<>(): group.getTechnicalStack());
        try{
            groupMapper.createGroup(group);
            groupMapper.insertStuIntoGps(validIds, group.getGroupId());
            System.err.println(validIds);
            System.err.println(group.getGroupId()); // 这个是对的
            return group.getGroupId();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return (long)-1;
    }
    public void createGroup(ObjectCountWrapper<Group> ocw, Long creatorId, Predicate<Long> accessProject){
        // FUNC: 给行projectId，调用者是否有权限使用
        if(!accessProject.test(ocw.getObj().getProjectId())){
            throw new AccessDeniedException("无权创建小组");
        }
        if(ocw.getObj().getGroupName() == null || ocw.getObj().getMaxsize() == null || ocw.getObj().getInstructorId() == null)
            throw new InvalidFormException("maxsize、groupName、projectId, instructorId不能为空");
        if(!ocw.getObj().getReportTime().isAfter(LocalDateTime.now()))
            throw new InvalidFormException("报告时间不能早于当前时间");

        Long instructorId = ocw.getObj().getInstructorId();
        User usr = usersMapper.findUserById(instructorId);
        if(usr == null || usr.getIdentity() > 2)
            throw new InvalidFormException("instructorId不存在或不合法");
        List<Group> groups = Stream.generate(ocw::getObj).limit(ocw.getCount()).toList();
        // 设置时间
        groups.forEach(g -> {
            g.setCreatorId(creatorId);
            g.setTeamTime(LocalDateTime.now());
        });
        groupMapper.createGroups(groups);
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
            if(group.getInstructorId() == null || projectMapper.checkTaInProj(pjId, group.getInstructorId()) == null)
                throw new InvalidFormException("无效的instructorId");
            if(group.getLeaderId() != null &&  projectMapper.checkStuInProj(group.getLeaderId(), pjId) == null)
                throw new InvalidFormException("无效的leaderId");
            if(group.getDeadline() == null || group.getDeadline().isBefore(LocalDateTime.now()))
                throw new InvalidFormException("无效的ddl");
            try {
                groupMapper.updateGroupForTea(group);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            throw new AccessDeniedException("无权修改小组信息");
    }

    public void ackInvitation(Long noticeId, Long stuId){
        Notice notice = noticeService.findNoticeById(noticeId);
        Set<Long> views = new HashSet<>(noticeMapper.findStuOfNotice(noticeId));
        if(!views.contains(stuId))
            throw new AccessDeniedException("无权访问该notice");
        if(notice == null)
            throw new InvalidFormException("无效的noticeid");
        try{
            String [] content = notice.getContent().split("[<>]");
            stuJoinGpSync(Long.parseLong(content[3]), stuId, false);
            StringBuilder sb = new StringBuilder();
            content[5] = "Accept";
            for(int i = 0; i < content.length; i++){
                if(i % 2 == 0)
                    sb.append(content[i]).append("<");
                else
                    sb.append(content[i]).append(">");
            }
            noticeMapper.updateNotice(notice);

        }catch (IndexOutOfBoundsException | NumberFormatException | PSQLException e){
            throw new InvalidFormException("无效的noticeid");
        }
    }

    public void nakInvitationOrApplication(Long noticeId, Long stuId){
        Notice notice = noticeService.findNoticeById(noticeId);
        Set<Long> views = new HashSet<>(noticeMapper.findStuOfNotice(noticeId));
        if(!views.contains(stuId))
            throw new AccessDeniedException("无权访问该notice");
        if(notice == null)
            throw new InvalidFormException("无效的noticeid");
        try{
            String [] content = notice.getContent().split("[<>]");
            StringBuilder sb = new StringBuilder();
            content[5] = "Reject";
            for(int i = 0; i < content.length; i++){
                if(i % 2 == 0)
                    sb.append(content[i]).append("<");
                else
                    sb.append(content[i]).append(">");
            }
            noticeMapper.updateNotice(notice);

        }catch (IndexOutOfBoundsException | NumberFormatException | PSQLException e){
            throw new InvalidFormException("无效的noticeid");
        }
    }

    public void ackApplication(Long noticeId, Long stuId){
        Notice notice = noticeService.findNoticeById(noticeId);
        Set<Long> views = new HashSet<>(noticeMapper.findStuOfNotice(noticeId));
        if(!views.contains(stuId))
            throw new AccessDeniedException("无权访问该notice");
        if(notice == null)
            throw new InvalidFormException("无效的noticeid");
        try{
            String [] content = notice.getContent().split("[<>]");
            Group gp = groupMapper.findGroupById(Long.parseLong(content[3]));
            if(gp == null || gp.getLeaderId() != stuId)
                throw new AccessDeniedException("无权访问该小组");
            stuJoinGpSync(Long.parseLong(content[3]), Long.parseLong(content[1]), false);
            StringBuilder sb = new StringBuilder();
            content[5] = "Accept";
            for(int i = 0; i < content.length; i++){
                if(i % 2 == 0)
                    sb.append(content[i]).append("<");
                else
                    sb.append(content[i]).append(">");
            }
            noticeMapper.updateNotice(notice);

        }catch (IndexOutOfBoundsException | NumberFormatException | PSQLException e){
            throw new InvalidFormException("无效的noticeid");
        }
    }


    public synchronized void stuJoinGpSync(Long gpId, Long stuId, boolean needApp){
        Group gp = groupMapper.findGroupById(gpId);
        int cnt = groupMapper.findCntOfStuInGroup(gpId);
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
            noticeService.createApplicationNotice(stuId, gp.getLeaderId(), gp.getProjectId(),
                gp.getGroupId());
        }else {
            groupMapper.stuJoinGroup(stuId, gpId);
        }
    }
    public void recruitMem(KeyValueWrapper<Long, List<Long>> gpId_stuIds, Long userId){
        //FUNC: 确定userId在group中
        Group group = groupMapper.findGroupById(gpId_stuIds.getKey());
        if(group == null || Objects.equals(
            groupMapper.findGroupOfStuInProject(userId, group.getProjectId()).getGroupId(),
            group.getGroupId())){
            throw new InvalidFormException("无效的groupId");
        }
        //PROC: 筛选stuIds
        List<Long> stuIds = gpId_stuIds.getValue().stream().filter(e -> projectMapper.checkStuInProj(e, group.getProjectId()) != null).toList();
        noticeService.createRecruitmentNotice(userId, stuIds, group.getProjectId(), group.getGroupId());

    }
    public void joinGroup(Long groupId, Long stuId){
        //PROC: 检查小组存在 -> 检查学生是不是在group对应的proj中 -> 检查是否已经加入小组 -> 成功加入
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
        stuJoinGpSync(gp.getGroupId(), stuId, true);
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
            groupMapper.updateVisibility(userId, groupId, visibility);
        } catch (Exception e) {
            throw new InvalidFormException("leaderId需要为long");
        }
    }

    public void removeMember(long leaderId,long groupId, long memberId){
        Group group = groupMapper.findGroupById(groupId);
        if (group == null){
            throw new InvalidFormException("小组不存在");
        }
        if (leaderId != group.getLeaderId()){
            throw new AccessDeniedException("无权修改小组信息");
        }
        if(groupMapper.findGroupIdOfUserInAProj(memberId, group.getProjectId()) == null ||
                !groupMapper.findGroupIdOfUserInAProj(memberId, group.getProjectId()).equals(group.getGroupId())){
            throw new AccessDeniedException("所选成员不在小组中");
        }
        try {
            groupMapper.removeMember(groupId,memberId);
        } catch (Exception e) {
            throw new InvalidFormException("leaderId需要为long");
        }
        noticeService.createRemoveNotice(leaderId,memberId,group.getProjectId(),groupId);
    }

    public void groupLeaderTransfer(long leaderId,long groupId, long memberId){
        Group group = groupMapper.findGroupById(groupId);
        if (group == null){
            throw new InvalidFormException("小组不存在");
        }
        if (leaderId != group.getLeaderId()){
            throw new AccessDeniedException("无权修改小组信息");
        }
         if(groupMapper.findGroupIdOfUserInAProj(memberId, group.getProjectId()) == null ||
                !groupMapper.findGroupIdOfUserInAProj(memberId, group.getProjectId()).equals(group.getGroupId())){
            throw new AccessDeniedException("所选成员不在小组中");
        }
        try {
            groupMapper.updateLeader(memberId,groupId);

        } catch (Exception e) {
            throw new InvalidFormException("leaderId需要为long");
        }
        noticeService.createTransferNotice(leaderId,memberId,group.getProjectId(),groupId);
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

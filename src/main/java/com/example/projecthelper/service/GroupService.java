package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;

import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.Wrappers.ObjectCountWrapper;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import java.util.function.Predicate;
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

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    //TODO:创建group

    public void createGroup(Group group, Long creatorId, Predicate<Long> accessProject){
        // FUNC: 给行projectId，调用者是否有权限使用
        if(!accessProject.test(group.getProjectId())){
            throw new AccessDeniedException("无权创建小组");
        }
        if(!group.getReportTime().isAfter(LocalDateTime.now()))
            throw new InvalidFormException("报告时间不能早于当前时间");
        User usr = usersMapper.findUserById(group.getInstructorId());
        if(usr == null || usr.getIdentity() > 2)
            throw new InvalidFormException("instructorId不存在或不合法");
        try{
            group.setCreatorId(creatorId);
            group.setTeamTime(LocalDateTime.now());
            groupMapper.createGroup(group);
            System.err.println(group.getGroupId()); // 这个是对的
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new InvalidFormException("maxsize、groupName、projectId。instructorId不能为空");
        }
    }
    public void createGroup(ObjectCountWrapper<Group> ocw, Long creatorId, Predicate<Long> accessProject){
        // FUNC: 给行projectId，调用者是否有权限使用
        if(!accessProject.test(ocw.getObj().getProjectId())){
            throw new AccessDeniedException("无权创建小组");
        }
        if(ocw.getObj().getGroupName() == null || ocw.getObj().getMaxsize() == null || ocw.getObj().getInstructorId() == null)
            throw new InvalidFormException("maxsize、groupName、projectId。instructorId不能为空");
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


    public void updateGroupForTea(Group group, Predicate<Long> accessGroup) {
        //此处存疑，前端能在group里装多少信息，是否能包括group的创建者（是否需要查询数据库获取创建者
        if (accessGroup.test(group.getGroupId())) {
            try {
                groupMapper.updateGroupForTea(group);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            throw new AccessDeniedException("无权修改小组信息");
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
        groupMapper.stuJoinGroup(stuId, groupId);
    }
    public void leaveGroup(Long stuId){
        groupMapper.stuLeaveGroup(stuId);
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

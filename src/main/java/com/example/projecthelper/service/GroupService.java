package com.example.projecthelper.service;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private ProjectMapper projectMapper;

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    //TODO:创建group

    public long createGroup(Group group){
        //引入了查询project创建者的方法在这里
        long creatorId = projectMapper.findTeacherByProject(group.getProjectId());
        group.setTeamTime(new Timestamp(new Date().getTime()));
        try{
            // 如果组名称为空，则设置组名称为new group
            if(group.getGroupName() == null)
                group.setGroupName("new group");
            // 调用groupMapper的createGroup方法，创建组
            long Id = groupMapper.createGroup(group);
            System.err.println("Id:"+Id); // 这个是错的
            System.err.println(group.getGroupId()); // 这个是对的
            // 返回组ID
            return group.getGroupId();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        // 如果出现异常，返回0
        return 0;
    }

    //TODO:修改group的信息
    public void modifyGroupInfo(Group group){

    }

    //TODO:学生加入group
    public void joinGroup(Group group, String jwt){

    }
//    public long[] createPluralGroup( long max_size,
//                                    long project_id, Timestamp team_time, Timestamp deadline, int number) {
//        long[] GroupIds = new long[number];
//        for (int i = 0 ; i <GroupIds.length;i++) {
//            String group_name = "new Group";
//            try {
//                GroupIds[i] = groupMapper.createGroup( max_size, group_name, project_id, team_time, deadline);
//            } catch (PSQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return GroupIds;
//    }

//    public long createOneGroup(long max_size,long project_id,
//                               Timestamp team_time, Timestamp deadline, String group_name) {
//        if (group_name == null) {
//            group_name = "new Group";
//        }
//        try {
//            return groupMapper.createGroup( max_size, group_name, project_id, team_time, deadline);
//        } catch (PSQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void updateGroupForTea(Group group, long user_id) {
        //此处存疑，前端能在group里装多少信息，是否能包括group的创建者（是否需要查询数据库获取创建者
        long creator_id;
        creator_id = groupMapper.findCreatorByGroup(group.getGroupId());
        if (creator_id == user_id) {
            try {
                groupMapper.updateGroupForTea(group);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
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


    public void stuLeaveGroup(long group_id,long stu_id){
        groupMapper.stuLeaveGroup(group_id, stu_id);
    }

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

}

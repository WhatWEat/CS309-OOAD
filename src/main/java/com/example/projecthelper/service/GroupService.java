package com.example.projecthelper.service;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.example.projecthelper.mapper.GroupMapper;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;

    //TODO:创建group
    public void createGroup(Group group){
        String newId = UUID.randomUUID().toString();
    }

    //TODO:修改group的信息
    public void modifyGroupInfo(Group group){

    }

    //TODO:学生加入group
    public void joinGroup(Group group, String jwt){

    }
    public long[] createPluralGroup( long max_size,
                                    long project_id, Timestamp team_time, Timestamp deadline, int number) {
        long[] GroupIds = new long[number];
        for (int i = 0 ; i <GroupIds.length;i++) {
            String group_name = "new Group";
            try {
                GroupIds[i] = groupMapper.createGroup( max_size, group_name, project_id, team_time, deadline);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
        }
        return GroupIds;
    }

    public long createOneGroup(long max_size,long project_id,
                               Timestamp team_time, Timestamp deadline, String group_name) {
        if (group_name == null) {
            group_name = "new Group";
        }
        try {
            return groupMapper.createGroup( max_size, group_name, project_id, team_time, deadline);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGroupSize(long max_size, long group_id) {
        try {
            groupMapper.updateGroupSize(max_size, group_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public void updateGroupInstructor(long instructor_id, long group_id) {
//        try {
//            groupMapper.updateGroupInstructor(instructor_id, group_id);
//        } catch (PSQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void updateGroupName(String group_name, long group_id) {
        try {
            groupMapper.updateGroupName(group_name, group_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGroupTime(Timestamp team_time, long group_id) {
        try {
            groupMapper.updateGroupTime(team_time, group_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGroupDeadline(Timestamp deadline, long group_id) {
        try {
            groupMapper.updateGroupDeadline(deadline, group_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void stuJoinGroup(long group_id,long stu_id){
        try {
            groupMapper.stuJoinGroup(group_id, stu_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void stuLeaveGroup(long group_id,long stu_id){
        try {
            groupMapper.stuLeaveGroup(group_id, stu_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Group findGroupOfStuInProject(long stu_id, long project_id){
        try {
            return groupMapper.findGroupOfStuInProject(stu_id,project_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Group> findUndermannedGroup(long project_id){
        try {
            return groupMapper.findUndermannedGroup(project_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Group> findAllGroup(long project_id){
        try {
            return groupMapper.findAllGroup(project_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int findMemberOfGroup(long group_id){
        try {
            return groupMapper.findMemberOfGroup(group_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

}

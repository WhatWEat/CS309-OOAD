package com.example.projecthelper.service;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;

    public long[] createPluralGroup(long instructor_id, long max_size,
                                    long project_id, Timestamp team_time, Timestamp deadline, int number) {
        long[] GroupIds = new long[number];
        for (long groupIds : GroupIds) {
            String group_name = "new Group";
            groupIds = groupMapper.createGroup(instructor_id, max_size, group_name, project_id, team_time, deadline);
        }
        return GroupIds;
    }

    public long createOneGroup(long instructor_id, long max_size,long project_id,
                               Timestamp team_time, Timestamp deadline, String group_name) {
        if (group_name == null) {
            group_name = "new Group";
        }
        return groupMapper.createGroup(instructor_id, max_size, group_name, project_id, team_time, deadline);
    }

    public void updateGroupSize(long max_size, long group_id) {
        groupMapper.updateGroupSize(max_size, group_id);
    }

    public void updateGroupInstructor(long instructor_id, long group_id) {
        groupMapper.updateGroupInstructor(instructor_id, group_id);
    }

    public void updateGroupName(String group_name, long group_id) {
        groupMapper.updateGroupName(group_name, group_id);
    }

    public void updateGroupTime(Timestamp team_time, long group_id) {
        groupMapper.updateGroupTime(team_time, group_id);
    }

    public void updateGroupDeadline(Timestamp deadline, long group_id) {
        groupMapper.updateGroupDeadline(deadline, group_id);
    }

    public void stuJoinGroup(long group_id,long stu_id){
        groupMapper.stuJoinGroup(group_id, stu_id);
    }

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

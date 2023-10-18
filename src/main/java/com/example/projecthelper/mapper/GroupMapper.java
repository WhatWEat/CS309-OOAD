package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Group;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {
    //TODO:修改部分传参过多的方法

    @Insert("insert into groups ( max_size,group_name, project_id, team_time, report_time,instructor_id)" +
            "VALUES (#{maxsize},#{groupName},#{projectId},#{teamTime},#{reportTime},#{instructorId});")
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "group_id")
    //max_size、group_name、project_id。instructor_id不能为空
    long createGroup (Group group) throws PSQLException;

    @Select("select instructor_id from group where group_id = #{group_id}")
    long findCreatorByGroup(long group_id);
    @Select("select leader_id from group where group_id = #{group_id}")
    long findLeaderByGroup(long group_id);

    @Update("update groups set " +
            "group_name =#{groupName} " +
            "leader_id =#{leaderId} " +
            "max_size =#{maxSize} " +
            "description =#{description} " +
            "team_time =#{teamTime} " +
            "report_time =#{reportTime} " +
            "where group_id = #{groupId};")
    //max_size、group_name、project_id。instructor_id不能为空
    void updateGroupForTea(Group group) throws PSQLException;

    @Update("update groups set " +
            "group_name =#{groupName} " +
            "description =#{description} " +
            "where group_id = #{groupId};")
    void updateGroupForLeader(Group group) throws PSQLException;

    @Delete("delete from stuingroup where group_id = #{group_id} and stu_id = #{stu_id};")
    void stuLeaveGroup(long group_id,long stu_id);

    @Select("""
            select * from groups where group_id in (
                select group_id from stuingroup where stu_id = #{stu_id}
                ) and project_id = #{project_id};""")
    Group findGroupOfStuInProject(long stu_id, long project_id);

    @Select("""
            SELECT g.group_id,leader_id,instructor_id , group_name, max_size, project_id, team_time, report_time
            FROM groups g
            LEFT JOIN stuInGroup s ON g.group_id = s.group_id
            WHERE g.project_id = #{project_id}
            GROUP BY g.group_id, g.group_name, g.max_size, g.project_id
            HAVING g.max_size > COUNT(s.stu_id);""")
    List<Group> findUndermannedGroup(long project_id );

    @Select("select * from groups where project_id = #{project_id};")
    List<Group> findAllGroup(long project_id);

    @Select("select count(*) from stuingroup where group_id = #{group_id};")
    int findMemberOfGroup(long group_id);
}

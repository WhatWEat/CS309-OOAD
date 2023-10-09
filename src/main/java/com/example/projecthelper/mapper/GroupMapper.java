package com.example.projecthelper.mapper;

import com.example.projecthelper.entity.Group;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface GroupMapper {

    @Insert("insert into groups (instructor_id, max_size,group_name, project_id, team_time, deadline)" +
            "VALUES (#{instructor_id},#{max_size},#{group_name},#{project_id},#{team_time},#{deadline});")
    @Options(useGeneratedKeys = true, keyProperty = "group_id", keyColumn = "group_id")
    long createGroup (long instructor_id, long max_size, String group_name,
                     long project_id, Timestamp team_time, Timestamp deadline);

    @Update("update groups set max_size =#{max_size} where group_id = #{group_id};")
    void updateGroupSize(long max_size, long group_id);

    @Update("update groups set instructor_id =#{instructor_id} where group_id = #{group_id};")
    void updateGroupInstructor(long instructor_id, long group_id);

    @Update("update groups set group_name =#{group_name} where group_id = #{group_id};")
    void updateGroupName(String group_name, long group_id);

    @Update("update groups set team_time =#{team_time} where group_id = #{group_id};")
    void updateGroupTime(Timestamp team_time, long group_id);

    @Update("update groups set deadline =#{deadline} where group_id = #{group_id};")
    void updateGroupDeadline(Timestamp deadline, long group_id);

    @Insert("insert into stuingroup (group_id, stu_id) VALUES (#{group_id},#{stu_id});")
    void stuJoinGroup(long group_id,long stu_id);

    @Delete("delete from stuingroup where group_id = #{group_id} and stu_id = #{stu_id};")
    void stuLeaveGroup(long group_id,long stu_id);

    @Select("""
            select * from groups where group_id in (
                select group_id from stuingroup where stu_id = #{stu_id}
                ) and project_id = #{project_id};""")
    Group findGroupOfStuInProject(long stu_id, long project_id);

    @Select("""
            SELECT g.group_id, group_name, instructor_id, max_size, project_id, team_time, deadline
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

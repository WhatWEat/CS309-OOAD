package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {


    @Select("select u.* from users u join stuinproject s on u.userid = s.projectid where s.projectid = #{projId} limit #{limit} offset #{offset};")
    List<User> getStuListOfProj(Long projId, int limit, int offset);
    @Select("select u.* from users u join taofproject tp on u.userid = tp.projectid where tp.projectid = #{projId};")
    List<User> getTaListOfProj(Long projId);
    //TODO:修改部分传参过多的方法

    @Insert("insert into groups (maxsize,groupName, projectId, teamTime, reportTime,instructorId, creatorId, description)" +
            "VALUES (#{maxsize},#{groupName},#{projectId},#{teamTime},#{reportTime},#{instructorId}, #{creatorId}, #{description});")
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "groupid")
    //maxsize、groupName、projectId。instructorId不能为空
    void createGroup (Group group) throws PSQLException;

    @Insert({
        "<script>",
        "INSERT INTO groups (maxsize, groupName, projectId, teamTime, reportTime, instructorId, creatorId, description) VALUES",
        "<foreach item='group' index='index' collection='groupList' separator=','>",
        "(#{group.maxsize}, #{group.groupName}, #{group.projectId}, #{group.teamTime}, #{group.reportTime}, #{group.instructorId}, #{group.creatorId}, #{description})",
        "</foreach>",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "groupid")
    void createGroups(@Param("groupList") List<Group> groupList);

    @Select("select creatorId from groups where groupId = #{groupId}")
    Long findCreatorByGroup(long groupId);

    @Select("select projectid from groups where groupid = #{groupId};")
    Long findPjIdOfGroup(Long groupId);

    @Select("select leaderId from groups where groupId = #{groupId}")
    long findLeaderByGroup(long groupId);

    @Select("select * from groups where groupid = #{groupId}")
    Group findGroupById(long groupId);

    @Select("select g.groupId from groups g join stuInGroup sIG on g.groupId = sIG.groupId where g.projectId = #{projectId} and sIG.stuId = #{userId}; ")
    Long findGroupIdOfUserInAProj(long userId, long projectId);

    @Insert("insert into stuInGroup values (#{groupId}, #{stuId})")
    void stuJoinGroup(Long stuId, Long groupId);
    @Select("select count(*) from stuInGroup where groupId = #{groupId}")
    int findCntOfStuInGroup(long groupId);
    @Delete("delete from stuingroup where stuId = #{stuId};")
    void stuLeaveGroup(long stuId);

    @Update("update groups set " +
            "groupName =#{groupName}, " +
            "leaderId =#{leaderId}, " +
            "maxsize =#{maxsize}, " +
            "description =#{description}, " +
            "teamTime =#{teamTime}, " +
            "reportTime =#{reportTime}, " +
            "instructorId =#{instructorId} " +
            "where groupId = #{groupId};")
    //max_size、group_name、instructor_id, groupId不能为空
    void updateGroupForTea(Group group) throws PSQLException;

    @Update("update groups set " +
            "groupName =#{groupName}, " +
            "description =#{description} " +
            "where groupId = #{groupId};")
    void updateGroupForLeader(Group group) throws PSQLException;



    @Select("""
            select * from groups where groupId in (
                select groupId from stuingroup where stuId = #{stuId}
                ) and projectId = #{projectId};""")
    Group findGroupOfStuInProject(long stuId, long projectId);

    @Select("""
            SELECT g.groupId,leaderId,instructorId , groupName, maxsize, projectId, teamTime, reportTime
            FROM groups g
            LEFT JOIN stuInGroup s ON g.groupId = s.groupId
            WHERE g.projectId = #{project_id}
            GROUP BY g.groupId, g.groupName, g.maxsize, g.projectId
            HAVING g.maxsize > COUNT(s.stuId);""")
    List<Group> findUndermannedGroup(long projectId );

    @Select("select * from groups where projectId = #{projectId};")
    List<Group> findAllGroup(long projectId);

    @Select("select count(*) from stuingroup where groupId = #{groupId};")
    int findMemberOfGroup(long groupId);

    @Update("update groups set leaderid = #{leaderId} where groupid = #{groupId};")
    void updateLeader(long leaderId, long groupId) throws PSQLException;

    @Update("UPDATE groups SET visibility = #{visibility} WHERE groupid = 1;\n")
    void updateVisibility(long userId, long groupId, Boolean[] visibility) throws PSQLException;

}

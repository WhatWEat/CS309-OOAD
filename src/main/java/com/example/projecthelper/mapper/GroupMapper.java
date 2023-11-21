package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.util.BooleanListArrayTypeHandler;
import com.example.projecthelper.util.StringListArrayTypeHandler;
import com.example.projecthelper.util.Wrappers.ObjectWrapper;
import java.util.Set;
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

    @Select("select g.*, u.name instructorName, u2.name leaderName " +
        "from groups g join users u on u.userid = g.instructorid join users u2 on u2.userid = g.leaderId where projectid = #{projId};")
    @Results({
        @Result(property = "technicalStack", column = "technicalstack", typeHandler = StringListArrayTypeHandler.class),
        @Result(property = "visibility", column = "visibility", typeHandler = BooleanListArrayTypeHandler.class)
    })
    List<Group> getBriefGroupsFromProj(Long projId);

    @Select("select stuid " +
        "from stuingroup where groupid = #{groupId} and stuid = #{stuId}; ")
    Long checkStuInGroup(Long groupId, Long stuId);

    @Select("select u.userId, u.name  " +
        "from stuingroup s join users u on s.stuid = u.userid where s.groupid = #{gpId};")
    List<User> getMembersFromGp(Long gpId);

    @Insert("insert into groups (maxsize,groupName, projectId, teamTime, reportTime," +
        "instructorId, creatorId, description, leaderId, technicalStack)" +
            "VALUES (#{maxsize},#{groupName},#{projectId},#{teamTime},#{reportTime}," +
        "#{instructorId}, #{creatorId}, #{description}, #{leaderId}, " +
        "#{technicalStack, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler});")
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "groupid")
    //maxsize、groupName、projectId。instructorId不能为空
    void createGroup (Group group) throws PSQLException;

    @Insert({
        "<script>",
        "INSERT INTO groups (maxsize, groupName, projectId, teamTime, reportTime, instructorId, creatorId, description, technicalStack) VALUES",
        "<foreach item='group' index='index' collection='groupList' separator=','>",
        "(#{group.maxsize}, #{group.groupName}, #{group.projectId}, #{group.teamTime}, " +
            "#{group.reportTime}, #{group.instructorId}, #{group.creatorId}, #{description}, " +
            "#{technicalStack, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler})",
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

    @Select("select g.*, u.name instructorName, u2.name leaderName from groups g join users u on u.userid = g.instructorid join users u2 on u2.userid = g.leaderId where groupid = #{groupId}")
    @Results({
        @Result(property = "technicalStack", column = "technicalstack", typeHandler = StringListArrayTypeHandler.class),
        @Result(property = "visibility", column = "visibility", typeHandler = BooleanListArrayTypeHandler.class)
    })
    Group findGroupById(long groupId);

    @Select("select g.groupId from groups g join stuInGroup sIG on g.groupId = sIG.groupId where g.projectId = #{projectId} and sIG.stuId = #{userId}; ")
    Long findGroupIdOfUserInAProj(long userId, long projectId);

    @Insert({
        "<script>",
        "INSERT INTO stuInGroup (groupId, stuId) VALUES",
        "<foreach item='stuId' index='index' collection='memberIds' separator=','>",
        "(#{gpId}, #{stuId})",
        "</foreach>",
        "</script>"
    })
    void insertStuIntoGps(@Param("memberIds") Set<Long> memberIds, Long gpId);

    @Insert("insert into stuInGroup values (#{groupId}, #{stuId})")
    void stuJoinGroup(Long stuId, Long groupId);
    @Select("select count(*) from stuInGroup where groupId = #{groupId}")
    int findCntOfStuInGroup(long groupId);
    @Delete("delete from stuingroup where stuId = #{stuId} and groupId = #{gpId};")
    void stuLeaveGroup(long stuId, long gpId);

    @Update("update groups set " +
            "groupName =#{groupName}, " +
            "leaderId =#{leaderId}, " +
            "maxsize =#{maxsize}, " +
            "description =#{description}, " +
            "reportTime =#{reportTime}, " +
            "instructorId =#{instructorId} " +
            "where groupId = #{groupId};")
    //max_size、group_name、instructor_id, groupId不能为空
    void updateGroupForTea(Group group) throws PSQLException;

    @Update("update groups set " +
        "maxsize =#{maxsize} " +
        "where projectId = #{projectId};")
        //max_size、group_name、instructor_id, groupId不能为空
    void updateMaxsizeForAllGroups(Group group) throws PSQLException;

    @Update("update groups set " +
        "reportTime =#{reportTime} " +
        "where projectId = #{projectId};")
        //max_size、group_name、instructor_id, groupId不能为空
    void updateReportTimeForAllGroups(Group group) throws PSQLException;

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

    @Delete("delete from stuingroup where (groupid,stuid) = (#{groupid},#{stuid});\n")
    void removeMember(long groupid, long stuid);
}

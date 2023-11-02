package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.SubmittedAssignment;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

import java.util.List;

@Mapper
public interface SubmittedAssMapper extends BaseMapper<SubmittedAssignment> {
    @Insert("insert into submittedassignment(assignmentid, projectid, text, comment, filepath)\n" +
            "VALUES (#{assignmentId}, #{projectId}, #{text}, #{comment}, #{filepath});")

        //此处assignmentId、projectId不为空，text&comment长度为1000，filepath暂定为200
    @Options(useGeneratedKeys = true, keyProperty = "submitId")
    void submitAss(SubmittedAssignment submittedAssignment) throws PSQLException;

    @Insert("insert into stuassignment (assignmentid, stuid) VALUES (#{assignmentId},#{stuId});")
    void stuSubmitAss(long assignmentId,long stuId) throws PSQLException;

    @Insert("insert into groupassignment (assignmentid, groupid) VALUES (#{assignmentId},#{groupId}")
    void groupSubmitAss(long assignmentId,long groupId) throws PSQLException;

    @Delete("delete from submittedassignment where submitid = #{submitId};")
    void removeAss(long submitId);

    @Delete("delete from stuassignment where assignmentid = #{assignmentId} and stuid = #{stuId};")
    void removeStuAss(long assignmentId, long stuId);

    @Delete("delete from groupassignment where assignmentid = #{assignmentId} and groupid = #{groupId};")
    void removeGroupAss(long assignmentId, long groupId);

    @Select("""
            SELECT sa.submitId, sa.assignmentId, sa.grade, sa.projectId, sa.text, sa.comment, sa.filepath, sa.review
            FROM submittedAssignment sa
            JOIN stuSubmit ss ON sa.submitId = ss.submitId
            WHERE sa.projectId = #{projectId} AND ss.stuId = #{stuId};
            """)
    List<SubmittedAssignment> findStuSubByProject(long projectId, long stuId);

    @Select("""
            SELECT sa.submitId, sa.assignmentId, sa.grade, sa.projectId, sa.text, sa.comment, sa.filepath, sa.review
            FROM submittedAssignment sa
            JOIN groupSubmit gs ON sa.submitId = gs.submitId
            WHERE sa.projectId = #{projectId} AND gs.groupId = #{groupId};
            """)
    List<SubmittedAssignment> findGroupSubByProject( long projectId, long groupId);

    @Select("""
            SELECT sa.submitId, sa.assignmentId, sa.grade, sa.projectId, sa.text, sa.comment, sa.filepath, sa.review
            FROM submittedAssignment sa
            JOIN stuSubmit ss ON sa.submitId = ss.submitId
            WHERE sa.assignmentid = #{assignmentId} AND ss.stuId = #{stuId};
            """)
    SubmittedAssignment findStuSubByAss( long assignmentId, long stuId);

    @Select("""
            SELECT sa.submitId, sa.assignmentId, sa.grade, sa.projectId, sa.text, sa.comment, sa.filepath, sa.review
            FROM submittedAssignment sa
            JOIN groupSubmit gs ON sa.submitId = gs.submitId
            WHERE sa.assignmentid = #{assignmentId} AND gs.groupId = #{groupId};
            """)
    SubmittedAssignment findGroupSubByAss( long assignmentId, long groupId);


    @Select("select * from submittedassignment where projectid = #{projectId} and assignmentid = #{assignmentId};")
    List<SubmittedAssignment> findAllSub(long projectId,long assignmentId);

    @Select("select * from submittedassignment where submitid = #{submitId};")
    SubmittedAssignment viewSub(long submitId);

    @Update("UPDATE submittedAssignment SET grade = #{grade}, comment = #{comment}, review = #{review} WHERE submitid = #{submitId};")
    void gradeAss(float grade, String comment ,long submitId,String review) throws PSQLException;




}

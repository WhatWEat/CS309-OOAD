package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Evaluation;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.util.StringListArrayTypeHandler;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

import java.util.List;

@Mapper
public interface SubmittedAssMapper extends BaseMapper<SubmittedAssignment> {
    @Insert("insert into submittedassignment(assignmentId, submitterId, text, comment, filepaths, submittedTime)\n" +
            "VALUES (#{assignmentId}, #{submitterId}, #{text}, #{comment}, " +
            "#{filepaths, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler}, #{submittedTime});")

        //此处assignmentId、projectId不为空，text&comment长度为1000，filepath暂定为200
    void submitAss(SubmittedAssignment submittedAssignment) throws PSQLException;

    @Insert("insert into evaluation(assignmentId, commentGroup, commentedGroup,grade, content, commentTime)\n" +
            "VALUES (#{assignmentId}, #{commentGroup},#{commentedGroup},#{grade}, #{content}, now()) ;")
    void submitEva(Evaluation evaluation);

    @Delete("delete from submittedassignment where assignmentid = 1;")
    void deleteSubmittedAssByAssId(Long assId);

    @Delete("delete from submittedAssignment where assignmentId = #{assignmentId} and submitterId = #{submitterId};")
    void deleteOriginalSubmit(SubmittedAssignment submittedAssignment);

    @Delete("delete from evaluation where assignmentId = #{assignmentId} and commentgroup = #{commentGroup} " +
            "and commentedgroup = #{commentedGroup};")
    void deleteOriginalEva(Evaluation evaluation);

    @Select("select * from submittedAssignment where submitId = #{submitId}")
    SubmittedAssignment findSubmittedAssignmentById(long submitId);

//    @Insert("insert into stuassignment (assignmentId, stuid) VALUES (#{assignmentId},#{stuId});")
//    void stuSubmitAss(long assignmentId,long stuId) throws PSQLException;
//
//    @Insert("insert into groupassignment (assignmentid, groupid) VALUES (#{assignmentId},#{groupId}")
//    void groupSubmitAss(long assignmentId,long groupId) throws PSQLException;

    @Delete("delete from submittedassignment where assignmentId = #{assId} and submitterId = #{submitterId};")
    void removeAss(long assId, long submitterId);

    @Delete("delete from evaluation where assignmentId = #{assId} and commentgroup = #{commentgroup} and commentedgroup = #{commentedgroup} ;")
    void removeEva(long assId, long commentgroup, long commentedgroup);

//    @Delete("delete from stuassignment where assignmentid = #{assignmentId} and stuid = #{stuId};")
//    void removeStuAss(long assignmentId, long stuId);
//
//    @Delete("delete from groupassignment where assignmentid = #{assignmentId} and groupid = #{groupId};")
//    void removeGroupAss(long assignmentId, long groupId);

    @Select("""
            SELECT sa.assignmentid, grade, submitterid, text, comment, review, submittedtime, togroup, togroup
            FROM submittedAssignment sa
            join assignment a on sa.assignmentid = a.assignmentid
            WHERE type = 'i' and projectId = #{projectId} AND submitterid = #{stuId};
            """)

    List<SubmittedAssignment> findStuSubByProject(long projectId, long stuId);

    @Select("""
            SELECT sa.assignmentid, grade, submitterid, text, comment, review, submittedtime, togroup, togroup
            FROM submittedAssignment sa
            join assignment a on sa.assignmentid = a.assignmentid
            WHERE type = 'g' and projectId = #{projectId} AND submitterid = #{groupId};
            """)

    List<SubmittedAssignment> findGroupSubByProject(long projectId, long groupId);

    @Select("""
            SELECT sa.submitId, sa.assignmentId, sa.grade, sa.projectId, sa.text, sa.comment, sa.filepath, sa.review
            FROM submittedAssignment sa
            JOIN stuSubmit ss ON sa.submitId = ss.submitId
            WHERE sa.assignmentid = #{assignmentId} AND ss.stuId = #{stuId};
            """)
    @Results({
            @Result(property = "filepaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    SubmittedAssignment findStuSubByAss(long assignmentId, long stuId);

    @Select("""
            SELECT sa.submitId, sa.assignmentId, sa.grade, sa.projectId, sa.text, sa.comment, sa.filepath, sa.review
            FROM submittedAssignment sa
            JOIN groupSubmit gs ON sa.submitId = gs.submitId
            WHERE sa.assignmentid = #{assignmentId} AND gs.groupId = #{groupId};
            """)
    SubmittedAssignment findGroupSubByAss(long assignmentId, long groupId);


    @Select("select * from submittedassignment where assignmentid = #{assignmentId} " +
            "limit #{limit} offset #{offset};")
    @Results({
            @Result(property = "filepaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<SubmittedAssignment> findAllSub(long assignmentId, long limit, long offset);

    @Select("select * from submittedassignment where assignmentId = #{assId} and submitterId = #{submitterId};")
    @Results({
            @Result(property = "filepaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    SubmittedAssignment viewSub(long assId, long submitterId);

    @Update("UPDATE submittedAssignment SET grade = #{grade}, comment = #{comment}, review = #{review} " +
            "WHERE submitterid = #{submitterId} and assignmentId = #{assignmentId};")
    void gradeAss(SubmittedAssignment submittedAssignment) throws PSQLException;


//    @Update({
//            "<script>",
//            "UPDATE submittedAssignment SET (grade, comment, review) = ",
//            "<foreach item='sa' index='index' collection='sas' separator=','>",
//            "(#{sa.grade}, #{sa.comment}, #{sa.review})",
//            "</foreach>",
//            " WHERE assignmentId = #{assignmentId} AND submitterId IN ",
//            "<foreach item='sa' index='index' collection='sas' open='(' separator=',' close=')'>",
//            "#{sa.submitterId}",
//            "</foreach>",
//            "</script>"
//    })
//    void updateGrades(@Param("sas") List<SubmittedAssignment> sas,
//                      @Param("assignmentId") long assignmentId);

    @Update({
            "<script>",
            "UPDATE submittedAssignment",
            "<set>",
            "grade = CASE submitterId",
            "<foreach item='sa' index='index' collection='sas' separator=' '>",
            "WHEN #{sa.submitterId} THEN #{sa.grade}",
            "</foreach>",
            "END,",
            "comment = CASE submitterId",
            "<foreach item='sa' index='index' collection='sas' separator=' '>",
            "WHEN #{sa.submitterId} THEN #{sa.comment}",
            "</foreach>",
            "END,",
            "review = CASE submitterId",
            "<foreach item='sa' index='index' collection='sas' separator=' '>",
            "WHEN #{sa.submitterId} THEN #{sa.review}",
            "</foreach>",
            "END",
            "</set>",
            "WHERE assignmentId = #{assignmentId}",
            "AND submitterId IN",
            "<foreach item='sa' index='index' collection='sas' open='(' separator=',' close=')'>",
            "#{sa.submitterId}",
            "</foreach>",
            "</script>"
    })
    void updateGrades(@Param("sas") List<SubmittedAssignment> sas,
                      @Param("assignmentId") long assignmentId);


    //以下是与小组互评相关方法
    @Select("""
            <script>
                SELECT * FROM evaluation s
                WHERE commentedgroup IS NOT NULL
                    <if test='commentedgroup != null and !commentedgroup.isEmpty()'>
                        AND (
                            <foreach collection='commentedgroup' item='item' index='index' separator=' AND '>
                                s.commentedgroup = #{item}
                            </foreach>)
                    </if>
                    <if test='commentgroup != null and !commentgroup.isEmpty()'>
                        AND (
                            <foreach collection='commentgroup' item='item' index='index' separator=' AND '>
                                s.commentgroup = #{item}
                            </foreach>)
                    </if>
                    <if test='grade != null and !grade.isEmpty()'>
                        AND (
                            <foreach collection='grade' item='item' index='index' separator=' AND '>
                                s.grade = #{item}
                            </foreach>)
                    </if>
            </script>
            """)
    List<Evaluation> searchEvaluation(List<Long> commentgroup, List<Long> commentedgroup, List<Float> grade);

    @Select("SELECT AVG(grade) FROM evaluation WHERE commentedgroup = #{commentedgroup};")
    Double avgGrade(long commentedgroup);

    @Select("select * from evaluation where projectid = #{projectid} and commentgroup = #{commentgroup}")
    List<Evaluation> selectCommented(long projectid,long commentgroup);

}

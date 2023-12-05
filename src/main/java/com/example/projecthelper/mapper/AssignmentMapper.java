package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.util.StringListArrayTypeHandler;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.postgresql.util.PSQLException;

@Mapper
public interface AssignmentMapper extends BaseMapper<Assignment> {

    @Select("select count(*) " +
        "from project p join stuinproject s on s.projectid = p.projectid " +
        "join assignment a on a.projectid = p.projectid " +
        "                where stuid = #{stuId};")
    int getAssCntByStu(Long stuId);
    @Select("select count(*) " +
        "from project p join stuinproject s on s.projectid = p.projectid " +
        "join assignment a on a.projectid = p.projectid " +
        "                where stuid = #{stuId} and s.projectId = #{projId};")
    int getAssCntByStuAndProj(Long stuId, Long projId);

    @Select("select count(*) " +
        "    from project p join taofproject t on t.projectid = p.projectid " +
        "    join assignment a on a.projectid = p.projectid " +
        "    where taid = #{taId};")
    int getAssCntByTa(Long taId);
    @Select("select count(*) " +
        "    from project p join taofproject t on t.projectid = p.projectid " +
        "    join assignment a on a.projectid = p.projectid " +
        "    where taid = #{taId} and a.projectId = #{projId};")
    int getAssCntByTaAndProj(Long taId, Long projId);

    @Select("select count(*) " +
        "from project p join assignment a on a.projectid = p.projectid " +
        "where p.teacherid = #{teaId};")
    int getAssCntByTea(Long teaId);
    @Select("select count(*) " +
        "from project p join assignment a on a.projectid = p.projectid " +
        "where p.teacherid = #{teaId} and a.projectId = #{projId};")
    int getAssCntByTeaAndProj(Long teaId, Long projId);

    @Select("select count(*) " +
        "from assignment;")
    int getAssCntByAdm();
    @Select("select count(*) " +
        "from assignment where projectId = #{projId};")
    int getAssCntByAdmAndProj(Long projId);


    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join users u on a.creatorid = u.userid order by a.releaseTime desc limit #{limit} offset #{offset};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByAdm(int limit, int offset);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join users u on a.creatorid = u.userid where p.projectid = #{projectId} order by a.releaseTime desc limit #{limit} offset #{offset};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByProj(Long projectId, int limit, int offset);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join users u on a.creatorid = u.userid where p.projectid = #{projectId} order by a.releaseTime desc;")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByProjWithoutLimit(Long projectId);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join stuInProject s on p.projectid = s.projectid " +
        "join users u on a.creatorid = u.userid where s.stuId = #{stuId} order by a.releaseTime desc limit #{limit} offset #{offset};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByStu(Long stuId, int limit, int offset);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join taOfProject s on p.projectid = s.projectid " +
        "join users u on a.creatorid = u.userid where s.taId = #{taId} order by a.releaseTime desc limit #{limit} offset #{offset};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByTa(Long taId, int limit, int offset);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join stuInProject s on p.projectid = s.projectid " +
        "join users u on a.creatorid = u.userid where a.creatorId = #{teaId} order by a.releaseTime desc limit #{limit} offset #{offset};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByTea(Long teaId, int limit, int offset);


    @Insert("insert into assignment (title, fullMark, projectId, description, type, creatorId, deadline, releaseTime, requireExtension)\n" +
            "VALUES (#{title}, #{fullMark}, #{projectId}, #{description}, #{type}, #{creatorId}, #{deadline}, #{releaseTime}, #{requireExtension})")
    /*此处所有变量均不为空，其中title长度200，description为2000
    type仅有i(individual)、g(group)两种模式*/
    @Options(useGeneratedKeys = true, keyProperty = "assignmentId", keyColumn = "assignmentid")
    void createAss(Assignment assignment);

    @Delete("delete from assignment where assignmentid = #{assId};")
    void deleteAss(Long assId);

    @Update("update assignment set filePaths = #{filePaths, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler} where assignmentId = #{assId}")
    void updateFilePathOfAss(List<String> filePaths, Long assId);

    @Select("select * from assignment where assignmentid =#{assignmentId};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    Assignment findAssById(long assignmentId);


    @Select("select * from submittedAssignment where assignmentid =#{assignmentId} and submitterId = #{submitterId};")
    @Results({
        @Result(property = "filepaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    SubmittedAssignment findSubAssById(long assignmentId, long submitterId);

    @Select("select * from submittedAssignment where assignmentid =#{assignmentId} order by submittedTime limit 1;")
    @Results({
        @Result(property = "filepaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    SubmittedAssignment findLatestSubAssByAssId(long assignmentId);


}












package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Assignment;
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

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join users u on a.creatorid = u.userid where p.projectid = #{projectId} limit #{limit} offset #{offset};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByProj(Long projectId, Long limit, Long offset);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join stuInProject s on p.projectid = s.projectid " +
        "join users u on a.creatorid = u.userid where s.stuId = #{stuId};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByStu(Long stuId);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join taOfProject s on p.projectid = s.projectid " +
        "join users u on a.creatorid = u.userid where s.taId = #{taId};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByTa(Long taId);

    @Select("select a.*, p.name projectName, u.name creatorName " +
        "from assignment a " +
        "join project p on a.projectid = p.projectid " +
        "join stuInProject s on p.projectid = s.projectid " +
        "join users u on a.creatorid = u.userid where a.creatorId = #{teaId};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    List<Assignment> getAssByTea(Long teaId);


    @Insert("insert into assignment (title, fullMark, projectId, description, type, creatorId, deadline, releaseTime, requireExtension)\n" +
            "VALUES (#{title}, #{fullMark}, #{projectId}, #{description}, #{type}, #{creatorId}, #{deadline}, #{releaseTime}, #{requireExtension})")
    /*此处所有变量均不为空，其中title长度200，description为2000
    type仅有i(individual)、g(group)两种模式*/
    @Options(useGeneratedKeys = true, keyProperty = "assignmentId", keyColumn = "assignmentid")
    void createAss(Assignment assignment) throws PSQLException;

    @Delete("delete from assignment where assignmentid = #{assId};")
    void deleteAss(Long assId);



    @Update("update assignment set filePaths = #{filePaths, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler} where assignmentId = #{assId}")
    void updateFilePathOfAss(List<String> filePaths, Long assId);

    @Select("select * from assignment where assignmentid =#{assignmentId};")
    @Results({
        @Result(property = "filePaths", column = "filepaths", typeHandler = StringListArrayTypeHandler.class)
    })
    Assignment findAssById(long assignmentId);

}












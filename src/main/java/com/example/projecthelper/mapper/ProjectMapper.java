package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.util.StringListArrayTypeHandler;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import com.example.projecthelper.util.Wrappers.ObjectWrapper;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.postgresql.util.PSQLException;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    @Insert("insert into project (name, description, teacherId) values (#{name}, #{description}, #{teacherId});")
    //name , teacher_id均不为空
    void createProject(Project pj) throws PSQLException;

    @Select("select teacherId from project where projectId = #{projectId}")
    Long findTeacherByProject(Long projectId) ;

    @Select("select taId from taOfProject where projectId = #{projectId} and taId = #{taId}")
    Long checkTaInProj(Long projectId, Long taId);

    @Select("select stuId from stuInProject where projectId = #{projectId}")
    List<Long> findStuIdsByProject(Long projectId);

    @Insert({
        "<script>",
        "INSERT INTO stuInProject (projectId, stuId) VALUES ",
        "<foreach item='stuId' index='index' collection='stuIds' separator=','>",
        "(#{projectId}, #{stuId})",
        "</foreach>",
        "</script>"
    })
    void insertStuIds(@Param("projectId") Long projectId, @Param("stuIds") Set<Long> stuIds);

    @Select("select stuId from stuInProject where stuId = #{stuId} and projectId = #{projectId}; ")
    Long checkStuInProj(Long stuId, Long projectId);

    @Select("SELECT intendedteammates FROM stuInProject WHERE projectId = #{projectId} AND stuId = #{stuId};")
    @Results({
        @Result(property = "object", column = "intendedteammates", javaType = List.class, typeHandler = StringListArrayTypeHandler.class)
    })
    ObjectWrapper<List<String>> getIntendedTeammates(long projectId, Long stuId);

    @Update("update stuInProject set intendedTeammates = #{intendedTeammates, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler} where projectId = #{projectId} and stuId = #{stuId}; ")
    void setIntendedTeammates(long projectId, Long stuId, List<String> intendedTeammates);

    @Insert("insert into taOfProject(projectId, taId) values (#{projId}, #{taId});")
    void designateTaToProj(long projId, long taId);

    @Insert("delete from taOfProject where projectId = #{projId} and taId #{taId});")
    void removeTaFromProj(long projId, long taId);

}

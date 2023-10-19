package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Project;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.postgresql.util.PSQLException;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    @Insert("insert into project (name, description, teacherId) values (#{name}, #{description}, #{teacherId});")
    //name , teacher_id均不为空
    void createProject(Project pj) throws PSQLException;

    @Select("select teacherId from project where projectId = #{projectId}")
    Long findTeacherByProject(Long projectId) ;

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

}

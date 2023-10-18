package com.example.projecthelper.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.postgresql.util.PSQLException;

@Mapper
public interface ProjectMapper {
    @Insert("insert into project (name , teacher_id) values (#{name}, #{teacher_id});")
    //name , teacher_id均不为空
    void createProject(String name) throws PSQLException;

    @Select("select teacher_id from project where project_id = #{project_id}")
    long findTeacherByProject(long project_id) ;
}

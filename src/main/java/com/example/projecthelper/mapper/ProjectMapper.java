package com.example.projecthelper.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.postgresql.util.PSQLException;

@Mapper
public interface ProjectMapper {
    @Insert("insert into project (name) values (#{name});")
    void createProject(String name) throws PSQLException;
}

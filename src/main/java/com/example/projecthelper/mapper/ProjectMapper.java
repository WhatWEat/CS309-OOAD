package com.example.projecthelper.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper {
    @Insert("insert into project (name) values (#{name});")
    public void createProject(String name);
}

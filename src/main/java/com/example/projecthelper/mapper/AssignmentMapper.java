package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Assignment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.postgresql.util.PSQLException;

@Mapper
public interface AssignmentMapper extends BaseMapper<Assignment> {
    @Insert("insert into assignment (title, fullMark, projectId, description, type, creatorId)\n" +
            "VALUES (#{title}, #{fullMark}, #{projectId}, #{description}, #{type}, #{creatorId}")
    /*此处所有变量均不为空，其中title长度200，description为2000
    type仅有i(individual)、g(group)两种模式*/
    void createAss(Assignment assignment) throws PSQLException;

    @Select("select * from assignment where assignmentid =#{assignmentId};")
    Assignment findAssById(long assignmentId);

}

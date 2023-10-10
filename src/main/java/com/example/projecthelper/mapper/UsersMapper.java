package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.User;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

@Mapper
public interface UsersMapper extends BaseMapper<User> {
    @Select("select * from users where userId = #{userId};")
    User findUserById(int userId);

    @Insert("INSERT INTO users (identity, password, name, gender) " +
            "VALUES (#{identity}, #{password}, #{name},#{gender}) ")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void registerUser(User user) throws PSQLException;

    void createUser(User user);

    @Update("UPDATE users SET technology_stack = #{technology_stack},programming_skills = #{programming_skills}, " +
            "intended_teammates = #{intended_teammates} WHERE userId = #{userId};")
    void updateStuInformation(String technology_stack, String programming_skills,String intended_teammates, long userId);


}

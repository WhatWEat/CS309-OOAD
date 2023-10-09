package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersMapper extends BaseMapper<User> {
    @Select("select * from users where user_id = #{user_id};")
    User findUserById(long user_id);

    @Insert("INSERT INTO users (identity, password, name, gender) " +
            "VALUES (#{identity}, #{password}, #{name},#{gender}) ")
    @Options(useGeneratedKeys = true, keyProperty = "user_id")
    void registerUser(User user);

    void createUser(User user);

    @Update("UPDATE users SET technology_stack = #{technology_stack},programming_skills = #{programming_skills}, " +
            "intended_teammates = #{intended_teammates} WHERE user_id = #{user_id};")
    void updateStuInformation(String technology_stack, String programming_skills,String intended_teammates, long user_id);


}

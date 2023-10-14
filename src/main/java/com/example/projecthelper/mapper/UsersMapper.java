package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.User;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

@Mapper
public interface UsersMapper extends BaseMapper<User> {
    @Select("select * from users where userId = #{userId};")
    User findUserById(int userId) throws PSQLException;

    @Insert("INSERT INTO users (identity, password, name, gender) " +
            "VALUES (#{identity}, #{password}, #{name},#{gender}) ")
    /*identity, password, name, gender均不为空，identity为整数，暂时仅有0（学生）、1（教师）、2（管理员）三种角色
    password长度上限20，gender仅有一位：m(male)、f(female)*/
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void registerUser(User user) throws PSQLException;

    void createUser(User user);

    @Update("UPDATE users SET technology_stack = #{technology_stack},programming_skills = #{programming_skills}, " +
            "intended_teammates = #{intended_teammates} WHERE userId = #{userId};")
    void updateStuInformation(String technology_stack, String programming_skills,
                              String intended_teammates, long userId)throws PSQLException;


}

package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.User;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

@Mapper
public interface UsersMapper extends BaseMapper<User> {
    @Select("select * from users where user_id = #{userId};")
    User findUserById(int userId);

    @Insert("INSERT INTO users (identity, password, name, gender) " +
            "VALUES (#{identity}, #{password}, #{name},#{gender}) ")
    /*identity, password, name, gender均不为空，identity为整数
    password长度上限20，gender仅有一位：m(male)、f(female)*/
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void registerUser(User user) throws PSQLException;

    void createUser(User user);

    @Update("UPDATE users SET " +
            "password = #{password},"+
            "phone = #{phone},"+
            "mail = #{mail},"+
            "birthday = #{birthday},"+
            "technology_stack = #{technology_stack}," +
            "programming_skills = #{programming_skills}, " +
            "intended_teammates = #{intended_teammates} " +
            "WHERE user_id = #{userId};")
    //identity, password, name, gender均不为空，identity为整数
    void updateStuInformation(User user)throws PSQLException;


}

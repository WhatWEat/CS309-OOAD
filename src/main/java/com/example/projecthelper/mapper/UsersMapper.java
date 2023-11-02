package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

@Mapper
public interface UsersMapper extends BaseMapper<User> {
    @Select("select * from users where userId = #{userId};")
    User findUserById(Long userId);
    @Select({
        "<script>",
        "SELECT * FROM users",
        "WHERE userId IN",
        "<foreach item='id' index='index' collection='list' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    List<User> findUsersById(List<Long> userIds);

    @Select("select projectid from stuinproject where stuid= #{stuId};")
    List<Long> findProByStu(long stuId);


    @Insert("insert into stuinproject (projectid, stuid) VALUES (#{projectId},#{projectId});")
    void stuInProject(long projectId, long stuId);

    @Insert("INSERT INTO users (identity, password, name, gender) " +
            "VALUES (#{identity}, #{password}, #{name},#{gender}) ")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void registerUser(User user) throws PSQLException;

    @Insert({
        "<script>",
        "INSERT INTO users (identity, password, name, gender) VALUES",
        "<foreach collection='users' item='user' separator=','>",
        "(#{user.identity}, #{user.password}, #{user.name}, #{user.gender})",
        "</foreach>",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void registerUsers(List<User> users) throws PSQLException;


    void createUser(User user);

    @Update("UPDATE users SET " +
            "password = #{password},"+
            "phone = #{phone},"+
            "mail = #{mail},"+
            "birthday = #{birthday},"+
            "technologyStack = #{technologyStack}," +
            "programmingSkills = #{programmingSkills}, " +
            "intendedTeammates = #{intendedTeammates} " +
            "WHERE userId = #{userId};")
    //identity, password, name, gender均不为空，identity为整数
    void updateStuInformation(User user)throws PSQLException;


}

package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.util.StringListArrayTypeHandler;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

@Mapper
public interface UsersMapper extends BaseMapper<User> {
    @Select("select * from users where userId = #{userId};")
    @Results({
        @Result(property = "programmingSkills", column = "programmingskills", typeHandler = StringListArrayTypeHandler.class)
    })
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
    @Results({
        @Result(property = "programmingSkills", column = "programmingskills", typeHandler = StringListArrayTypeHandler.class)
    })
    List<User> findUsersById(List<Long> userIds);

    @Select("select projectid from stuinproject where stuid= #{stuId};")
    List<Long> findProByStu(long stuId);


    @Insert("insert into stuinproject (projectid, stuid) VALUES (#{projectId},#{projectId});")
    void stuInProject(long projectId, long stuId);

    @Insert("INSERT INTO users (userId, identity, password, name, gender) " +
            "VALUES (#{userId}, #{identity}, #{password}, #{name},#{gender}) ")
    void registerUser(User user) throws PSQLException;

    @Insert({
        "<script>",
        "INSERT INTO users (userId, identity, password, name, gender) VALUES",
        "<foreach collection='users' item='user' separator=','>",
        "(#{user.userId}, #{user.identity}, #{user.password}, #{user.name}, #{user.gender})",
        "</foreach>",
        "</script>"
    })
    void registerUsers(List<User> users) throws PSQLException;


    @Update("UPDATE users SET " +
            "phone = #{phone},"+
            "email = #{email},"+
            "name = #{name},"+
            "gender = #{gender},"+
            "birthday = #{birthday},"+
            "programmingSkills = #{programmingSkills, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler}, " +
            "avatarPath = #{avatarPath} "+
            "WHERE userId = #{userId};")
    //identity, password, name, gender均不为空，identity为整数
    void updateStuInformation(User user)throws PSQLException;

    @Update("update users set password = #{password} where userId = #{userId};")
    void changePass(Long userId, String password);

}

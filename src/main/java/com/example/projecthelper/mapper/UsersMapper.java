package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.util.StringListArrayTypeHandler;

import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import java.sql.Timestamp;
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
        "SELECT u.name FROM users u",
        "WHERE userId IN",
        "<foreach item='id' index='index' collection='userIds' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    List<String> findUsernamesById(List<Long> userIds);

    @Select("select * from users where identity = #{identity};")
    @Results({
        @Result(property = "programmingSkills", column = "programmingskills", typeHandler = StringListArrayTypeHandler.class)
    })
    List<User> findUsersByIdentity(Integer identity);

    @Select("select * from users u join taOfProject t on u.userId = t.taId where t.projectId = #{projId};")
    @Results({
        @Result(property = "programmingSkills", column = "programmingskills", typeHandler = StringListArrayTypeHandler.class)
    })
    List<User> findTaByProj(Long projId);

    @Select("select * from users u join stuinproject t on u.userId = t.stuid where t.projectId = #{projId};")
    @Results({
            @Result(property = "programmingSkills", column = "programmingskills", typeHandler = StringListArrayTypeHandler.class)
    })
    List<User> findStuByProj(Long projId);

    @Select({
        "<script>",
        "SELECT * FROM users",
        "WHERE userId IN",
        "<foreach item='id' index='index' collection='userIds' open='(' separator=',' close=')'>",
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
            "name = #{name},"+
            "email = #{email},"+
            "phone = #{phone},"+
            "gender = #{gender},"+
            "birthday = #{birthday},"+
            "programmingSkills = #{programmingSkills, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler}, " +
            "avatarPath = #{avatarPath} "+
            "WHERE userId = #{userId};")
    //identity, password, name, gender均不为空，identity为整数
    void updateStuInformation(User user)throws PSQLException;

    @Update("update users set email = #{email} where userId = #{userId};")
    void updateEmail(Long userId, String email);
    @Update("update users set phone = #{phone} where userId = #{userId};")
    void updatePhone(Long userId, String phone);

    @Update("update users set password = #{password} where userId = #{userId};")
    void changePass(Long userId, String password);

    @Update("update users set password = #{password} where email = #{email};")
    void changePassByEmail(String email, String password);

    @Update({
        "<script>",
        "  <foreach collection='users' item='user' separator=';'>",
        "    UPDATE users",
        "    SET password = #{user.password}",
        "    WHERE userId = #{user.userId} and identity > 1",
        "  </foreach>",
        "</script>"
    })
    void resetPass(List<User> users);

    @Update({
        "<script>",
        "update users set isFrozen = true ",
        "WHERE identity > 1 and userId IN ",
        "<foreach item='id' index='index' collection='userIds' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    void freezeUsers(List<Long> userIds);

    @Update({
        "<script>",
        "update users set isFrozen = false ",
        "WHERE identity > 1 and userId IN ",
        "<foreach item='id' index='index' collection='userIds' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    void unfreezeUsers(List<Long> userIds);

    @Select("select * from users where email = #{email};")
    User findUserByMail(String email);



}

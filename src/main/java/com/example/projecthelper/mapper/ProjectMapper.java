package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.util.StringListArrayTypeHandler;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import com.example.projecthelper.util.Wrappers.ObjectWrapper;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.postgresql.util.PSQLException;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    @Select("select count(*) from project p join stuinproject s on s.projectid = p.projectid where stuid = #{stuId};")
    int getProjCntByStu(Long stuId);

    @Select("select count(*) from project p join taofproject s on s.projectid = p.projectid where taid = #{taId};")
    int getProjCntByTa(Long taId);


    @Select("select count(*) from project p where teacherid= #{teaId};")
    int getProjCntByTea(Long teaId);

    @Select("select count(*) from project;")
    int getProjCntByAdm();

    @Select("select * from project where projectId = #{pjId}")
    Project findProjById(Long pjId);

    @Select({
        "<script>",
        "SELECT * FROM project",
        "WHERE projectId IN",
        "<foreach item='projectId' index='index' collection='projectIds' open='(' separator=',' close=')'>",
        "#{projectId}",
        "</foreach>",
        "</script>"
    })
    void findProjByIds(List<Long> projectIds);


    @Select("select p.*, u.name teacherName " +
        "from project p join users u on p.teacherid = u.userid limit #{limit} offset #{offset};")
    List<Project> getProjByAdm(int limit, int offset);

    @Select("select p.*, u.name teacherName " +
        "from project p join users u on p.teacherid = u.userid where p.teacherid = #{userId} limit #{limit} offset #{offset};")
    List<Project> getProjByTea(Long userId, int limit, int offset);

    @Select("select p.*, u.name teacherName " +
        "from project p " +
        "    join users u on p.teacherid = u.userid " +
        "join taofproject t on p.projectid = t.projectid where t.taid = #{userId} limit #{limit} offset #{offset};")
    List<Project> getProjByTa(Long userId, int limit, int offset);

    @Select("select p.*, u.name teacherName " +
        "from project p " +
        "    join users u on p.teacherid = u.userid " +
        "join stuinproject s on p.projectid = s.projectid where s.stuId = #{userId} limit #{limit} offset #{offset};")
    List<Project> getProjByStu(Long userId, int limit, int offset);

    @Insert("insert into project (name, description, teacherId) values (#{name}, #{description}, #{teacherId});")
    //name , teacher_id均不为空
    void createProject(Project pj) throws PSQLException;

    @Update("update project set (name, description) = (#{name}, #{description}) where projectid = #{projectId};")
        //name , teacher_id均不为空
    void editProj(Project pj);

    @Select("select teacherId from project where projectId = #{projectId}")
    Long findTeacherByProject(Long projectId) ;

    @Select("select taId from taOfProject where projectId = #{projectId} and taId = #{taId}")
    Long checkTaInProj(Long projectId, Long taId);

    @Select("select stuId from stuInProject where projectId = #{projectId}")
    List<Long> findStuIdsByProject(Long projectId);

    @Delete("delete from stuInProject where projectId = #{projectId}")
    void deleteAllStuFromProj(Long projectId);

    @Insert({
        "<script>",
        "INSERT INTO stuInProject (projectId, stuId) VALUES ",
        "<foreach item='stuId' index='index' collection='stuIds' separator=','>",
        "(#{projectId}, #{stuId})",
        "</foreach>",
        "</script>"
    })
    void insertStuIds(@Param("projectId") Long projectId, @Param("stuIds") Set<Long> stuIds);

    @Select("select stuId from stuInProject where stuId = #{stuId} and projectId = #{projectId}; ")
    Long checkStuInProj(Long stuId, Long projectId);

    @Select("SELECT intendedteammates FROM stuInProject WHERE projectId = #{projectId} AND stuId = #{stuId};")
    @Results({
        @Result(property = "object", column = "intendedteammates", javaType = List.class, typeHandler = StringListArrayTypeHandler.class)
    })
    ObjectWrapper<List<String>> getIntendedTeammates(long projectId, Long stuId);

    @Update("update stuInProject set intendedTeammates = #{intendedTeammates, jdbcType=ARRAY, typeHandler=com.example.projecthelper.util.StringListArrayTypeHandler} where projectId = #{projectId} and stuId = #{stuId}; ")
    void setIntendedTeammates(long projectId, Long stuId, List<String> intendedTeammates);

//    @Insert({
//        "<script>",
//        "insert into taOfProject(projectId, taId) values ",
//        "<foreach item='taId' index='index' collection='taIds' separator=','>",
//        "(#{projId}, #{taId})",
//        "</foreach>",
//        "</script>"
//    })
//    void designateTaToProj(long projId, List<Long> taIds);

    @Insert({
        "<script>",
        "insert into taOfProject (projectId, taId) values ",
        "<foreach item='taId' index='index' collection='taIds' separator=','>",
        "(#{projId}, #{taId})",
        "</foreach>",
        "</script>"
    })
    void designateTaToProj(long projId, List<Long> taIds);


    @Insert("delete from taOfProject where projectId = #{projId} and taId #{taId});")
    void removeTaFromProj(long projId, long taId);

}

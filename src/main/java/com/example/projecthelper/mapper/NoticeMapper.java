package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Notice;
import java.util.Set;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select * from notice where noticeId = #{noticeId};")
    Notice findNoticeById(Long noticeId);

    @Select("select creatorId from notice where noticeId = #{noticeId};")
    Long findCreatorByNotice(Long noticeId);

    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        " where " +
        "(title ilike #{key} or u.name ilike #{key} or content ilike #{key}) order by createTime desc limit #{limit} offset #{offset};;")
    List<Notice> findNoticeOfAdm(Long limit, Long offset, String key);


    //FUNC: 寻找一个学生在proj中的所有notice
    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join stuviewnotice s on n.noticeid = s.noticeid" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        " where s.stuid = #{stuId} and n.projectid = #{projId} and " +
        "(title ilike #{key} or u.name ilike #{key} or content ilike #{key}) order by createTime desc limit #{limit} offset #{offset} ;")
    List<Notice> findNoticeOfStuAndProj(Long stuId, Long projId, Long limit, Long offset, String key);

    //FUNC: 寻找一个学生的所有notice
    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join stuviewnotice s on n.noticeid = s.noticeid" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        " where s.stuid = #{stuId} and" +
        "(title ilike #{key} or u.name ilike #{key} or content ilike #{key}) order by createTime desc limit #{limit} offset #{offset};")
    List<Notice> findNoticeOfStu(Long stuId, Long limit, Long offset, String key);

    //FUNC: 寻找一个老师在proj中的所有notice
    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        " where p.teacherId = #{teaId} and n.projectid = #{projId} and type = 0 and" +
        "(title ilike #{key} or u.name ilike #{key} or content ilike #{key}) order by createTime desc limit #{limit} offset #{offset};")
    List<Notice> findNoticeOfTeaAndProj(Long teaId, Long projId, Long limit, Long offset, String key);

    //FUNC: 寻找一个老师的所有notice
    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        " where p.teacherId = #{teaId} and type = 0 and" +
        "(title ilike #{key} or u.name ilike #{key} or content ilike #{key}) order by createTime desc limit #{limit} offset #{offset};;")
    List<Notice> findNoticeOfTea(Long teaId, Long limit, Long offset, String key);

    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        "    join taOfProject t on t.projectid = n.projectid" +
        " where t.taId = #{taId} and n.projectid = #{projId} and type = 0 and" +
        "(title ilike #{key} or u.name ilike #{key} or content ilike #{key}) order by createTime desc limit #{limit} offset #{offset};")
    List<Notice> findNoticeOfTaAndProj(Long taId, Long projId, Long limit, Long offset, String key);

    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        "    join taOfProject t on t.projectid = n.projectid" +
        " where t.taId = #{taId} and type = 0 and" +
        "(title ilike #{key} or u.name ilike #{key} or content ilike #{key}) order by createTime desc limit #{limit} offset #{offset};;")
    List<Notice> findNoticeOfTa(Long taId, Long limit, Long offset, String key);


    @Select("select n.* from notice n join stuviewnotice s on s.noticeId = n.noticeId " +
        "where type = #{type} and status = 0 and creatorId = #{creatorId} and stuId = #{stuId}")
    Notice getPreviousUndecidedNotice(Long creatorId, Long stuId, int type);
    @Insert("insert into notice ( title, content, creatorId, projectId, createTime, type, status, groupId)\n" +
            "VALUES (#{title},#{content},#{creatorId},#{projectId}, #{createTime}, #{type}, #{status}, #{groupId});")
    @Options(useGeneratedKeys = true, keyProperty = "noticeId", keyColumn = "noticeid")
        //title、content、creatorId、projectId均不为空，title长度上限为200，content为5000
    void createNotice(Notice notice);

    @Insert("insert into stuviewnotice (noticeId, stuId) VALUES (#{noticeId}, #{stuId});")
    //二者同时不为空
    void stuViewNotice(Long noticeId, Long stuId) throws PSQLException;

    //FUNC: 一次性导入多个stuView
    @Insert({
        "<script>",
        "INSERT INTO stuviewnotice(noticeId, stuId) VALUES ",
        "<foreach collection='stuViewSet' item='stuId' index='index' separator=','>",
        "(#{noticeId}, #{stuId})",
        "</foreach>",
        "</script>"
    })
    void insertStuView(@Param("stuViewSet") Set<Long> stuViewSet, @Param("noticeId") Long noticeId);


    //FUNC: 一次性找出一个noticeId对应的所有userId
    @Select("select stuId from stuViewNotice where noticeId = #{noticeId};")
    List<Long> findStuOfNotice(Long noticeId);



    @Select("select n.noticeId from notice n" +
        "    join stuviewnotice s on n.noticeid = s.noticeid" +
        "    join project p on p.projectid= n.projectid" +
        " where (s.stuid = #{stuId} and n.type = 2 or n.creatorId = #{stuId} and n.type = 1) and n.projectid = #{projId} and status = 0;")
    List<Long> findUndecidedAppInvOfStuAndProj(Long stuId, Long projId);

    @Update({
        "<script>",
        "UPDATE notice SET status = #{status} ",
        "WHERE noticeId IN ",
        "<foreach collection='noticeIds' item='noticeId' open='(' separator=',' close=')'>",
        "#{noticeId}",
        "</foreach>",
        "</script>"
    })
    void updateNoticeStatusBySet(@Param("noticeIds") Set<Long> noticeIds, @Param("status") int status);


    @Update("UPDATE notice SET status = #{status} " +
        "WHERE noticeId = #{noticeId};")
        //title、content、creatorId均不为空，title长度上限为200，content为5000
    void updateNoticeStatus(Notice notice);

    @Update("UPDATE notice SET createTime = #{createTime} " +
        "WHERE noticeId = #{noticeId};")
        //title、content、creatorId均不为空，title长度上限为200，content为5000
    void updateNoticeTime(Notice notice);

    @Update("UPDATE notice SET title = #{title}, content = #{content} " +
            "WHERE noticeId = #{noticeId};")
    //title、content、creatorId均不为空，title长度上限为200，content为5000
    void updateNotice(Notice notice) throws PSQLException;

    @Delete("DELETE FROM notice WHERE noticeId = #{noticeId};")
    void deleteNotice(Long noticeId);

    @Delete("DELETE FROM stuViewNotice WHERE noticeId = #{noticeId} AND stuId = #{stuId};")
    void deleteStuViewNoticeByStu(Long noticeId, Long stuId);

    @Delete("DELETE FROM stuViewNotice WHERE noticeId = #{noticeId};")
    void deleteStuViewNoticeByNotice(Long noticeId);
}

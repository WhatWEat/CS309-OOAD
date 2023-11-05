package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Notice;
import java.util.Set;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select * from notice where noticeId = #{noticeId};")
    Notice findNoticeById(Long noticeId);

    @Select("select creatorId from notice where noticeId = #{noticeId};")
    Long findCreatorByNotice(Long noticeId);

    //FUNC: 寻找一个学生在proj中的所有notice
    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join stuviewnotice s on n.noticeid = s.noticeid" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        " where s.stuid = #{stuId} and n.projectid = #{projId} limit #{limit} offset #{offset};")
    List<Notice> findNoticeOfStuAndProj(Long stuId, Long projId, Long limit, Long offset);

    //FUNC: 寻找一个学生的所有notice
    @Select("select n.*, p.name projectName, u.name creatorName from notice n" +
        "    join stuviewnotice s on n.noticeid = s.noticeid" +
        "    join project p on p.projectid= n.projectid" +
        "    join users u on u.userid = n.creatorid" +
        " where s.stuid = #{stuId} limit #{limit} offset #{offset};")
    List<Notice> findNoticeOfStu(Long stuId, Long limit, Long offset);

    @Insert("insert into notice ( title, content, creatorId, projectId, createTime)\n" +
            "VALUES (#{title},#{content},#{creatorId},#{projectId}, #{createTime});")
    @Options(useGeneratedKeys = true, keyProperty = "noticeId", keyColumn = "noticeid")
        //title、content、creatorId、projectId均不为空，title长度上限为200，content为5000
    void createNotice(Notice notice) throws PSQLException;

    @Insert("insert into stuviewnotice (noticeId, stuId) VALUES (#{noticeId}, #{stuId});")
    //二者同时不为空
    void stuViewNotice(Long noticeId, Long stuId) throws PSQLException;

    //FUNC: 一次性导入多个stuView
    @Insert({
        "<script>",
        "INSERT INTO stuviewnotice(noticeId, stuId) VALUES",
        "<foreach collection='stuViewSet' item='item' index='index' separator=','>",
        "(#{noticeId}, #{item})",
        "</foreach>",
        "</script>"
    })
    void insertStuView(@Param("stuViewSet") Set<Long> stuViewSet, @Param("noticeId") Long noticeId);

    //FUNC: 一次性找出一个noticeId对应的所有userId
    @Select("select stuId from stuViewNotice where noticeId = #{noticeId};")
    List<Long> findStuOfNotice(Long noticeId);





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

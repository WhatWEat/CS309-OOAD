package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Notice;
import org.apache.ibatis.annotations.*;
import org.postgresql.util.PSQLException;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select * from notice where notice_id = #{noticeId};")
    Notice findNoticeById(long noticeId);

    @Select("select creator_id from notice where notice_id = #{notice_id};")
    long findCreatorByNotice(long notice_id);

    @Insert("insert into notice ( title, content, creator_id， project_id)\n" +
            "VALUES (#{title},#{content},#{creatorId},#{project_id});")
    //title、content、creatorId、project_id均不为空，title长度上限为200，content为5000
    void createNotice(Notice notice) throws PSQLException;

    @Insert("insert into stuviewnotice (notice_id, stu_id) VALUES (#{notice_id}, #{stu_id});")
    //二者同时不为空
    void stuViewNotice(long notice_id, long stu_id) throws PSQLException;

    @Update("UPDATE notice SET title = #{title}, content = #{content} " +
            "WHERE notice_id = #{noticeId};")
    //title、content、creatorId均不为空，title长度上限为200，content为5000
    void updateNotice(Notice notice) throws PSQLException;

    @Delete("DELETE FROM notice WHERE notice_id = #{noticeId};")
    void deleteNotice(long noticeId);

    @Delete("DELETE FROM stuViewNotice WHERE notice_id = #{notice_id} AND stu_id = #{stu_id};")
    void deleteStuViewNoticeByStu(long notice_id, long stu_id);

    @Delete("DELETE FROM stuViewNotice WHERE notice_id = #{notice_id};")
    void deleteStuViewNoticeByNotice(long notice_id);
}

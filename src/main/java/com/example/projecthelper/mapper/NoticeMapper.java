package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select * from notice where noticeId = #{noticeId};")
    Notice findNoticeById(long noticeId);

    @Select("select * from notice where creatorId = #{creatorId};")
    List<Notice> findNoticeByCreator(long creatorId);

    @Insert("insert into notice ( title, content, creatorId)\n" +
            "VALUES (#{title},#{content},#{creatorId});")
    void createNotice(String title,String content,long creatorId);

    @Insert("insert into stuviewnotice (notice_id, stu_id) VALUES (#{notice_id}, #{stu_id});")
    void stuViewNotice(long notice_id, long stu_id);

    @Update("UPDATE notice SET title = #{title}, content = #{content} " +
            "WHERE noticeId = #{noticeId};")
    void updateNotice(String title, String content, long noticeId);

    @Delete("DELETE FROM notice WHERE noticeId = #{noticeIId};")
    void deleteNotice(long noticeId);

    @Delete("DELETE FROM stuViewNotice WHERE notice_id = #{notice_id} AND stu_id = #{stu_id};")
    void deleteStuViewNoticeByStu(long notice_id, long stu_id);

    @Delete("DELETE FROM stuViewNotice WHERE notice_id = #{notice_id};")
    void deleteStuViewNoticeByNotice(long notice_id);
}

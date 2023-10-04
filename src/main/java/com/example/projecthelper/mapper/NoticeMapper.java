package com.example.projecthelper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.projecthelper.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select * from notice where notice_id = #{notice_id};")
    Notice findNoticeById(long notice_id);

    @Select("select * from notice where creator_id = #{creator_id};")
    List<Notice> findNoticeByCreator(long creator_id);

    @Insert("insert into notice ( title, content, creator_id)\n" +
            "VALUES (#{title},#{content},#{creator_id);")
    void createNotice(String title,String content,long creator_id);

    @Insert("insert into stuviewnotice (notice_id, stu_id) VALUES (#{notice_id}, #{stu_id});")
    void stuViewNotice(long notice_id, long stu_id);

    @Update("UPDATE notice SET title = #{title}, content = #{content} " +
            "WHERE notice_id = #{notice_id};")
    void updateNotice(String title, String content, long notice_id);

    @Delete("DELETE FROM notice WHERE notice_id = #{notice_id};")
    void deleteNotice(long notice_id);

    @Delete("DELETE FROM stuViewNotice WHERE notice_id = #{notice_id} AND stu_id = #{stu_id};")
    void deleteStuViewNoticeByStu(long notice_id, long stu_id);

    @Delete("DELETE FROM stuViewNotice WHERE notice_id = #{notice_id};")
    void deleteStuViewNoticeByNotice(long notice_id);
}

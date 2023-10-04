package com.example.projecthelper.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.NoticeMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {
    @Autowired
    private NoticeMapper noticeMapper;

    public void createNotice(String title,String content,Integer creator_id){
        noticeMapper.createNotice(title, content,creator_id);
    }

}

package com.example.projecthelper.service;

import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.util.ResponseResult;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    //TODO：发布Notice
    public void postNotice(Notice notice, Long creator_id){
        try {
            noticeMapper.createNotice(notice.getTitle(), notice.getContent(), creator_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO:先匹配jwt中的id与notice中的id，防止篡改他人内容。验证之后才进行更改Notice
    public boolean modifyNoticeWithUser(Notice notice, Long currentUserId){
        long creatorId = 0;
        try {
            creatorId = noticeMapper.findNoticeById(notice.getNoticeId()).getCreatorId();
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
        try {
            System.err.println(noticeMapper.findNoticeById(notice.getNoticeId()));
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
        if(creatorId != currentUserId)
            return false;
        try {
            noticeMapper.updateNotice(notice.getTitle(), notice.getContent(), notice.getNoticeId());
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public void createNotice(String title,String content,Integer creatorId){
        try {
            noticeMapper.createNotice(title, content,creatorId);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void stuViewNotice(long notice_id, long stu_id){
        try {
            noticeMapper.stuViewNotice(notice_id, stu_id);
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }

}

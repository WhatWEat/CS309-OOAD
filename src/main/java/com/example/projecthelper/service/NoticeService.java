package com.example.projecthelper.service;

import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    //TODO：发布Notice
    public void postNotice(Notice notice, Long creator_id){
            noticeMapper.createNotice(notice.getTitle(), notice.getContent(), creator_id);
    }

    //TODO:先匹配jwt中的id与notice中的id，防止篡改他人内容。验证之后才进行更改Notice
    public boolean modifyNoticeWithUser(Notice notice, Long currentUserId){
        long creatorId = noticeMapper.findNoticeById(notice.getNoticeId()).getCreatorId();
        System.err.println(noticeMapper.findNoticeById(notice.getNoticeId()));
        if(creatorId != currentUserId)
            return false;
        noticeMapper.updateNotice(notice.getTitle(), notice.getContent(), notice.getNoticeId());
        return true;
    }


    public void createNotice(String title,String content,Integer creatorId){
        noticeMapper.createNotice(title, content,creatorId);
    }

    public void stuViewNotice(long notice_id, long stu_id){
        noticeMapper.stuViewNotice(notice_id, stu_id);
    }

}

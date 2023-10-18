package com.example.projecthelper.service;

import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private ProjectMapper projectMapper;

    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    //TODO：发布Notice,现在creator_id已经重新移入notice类中
    public void postNotice(Notice notice, Long creator_id){
        long creatorId = projectMapper.findTeacherByProject(notice.getProjectId());
        if (creator_id == creatorId) {
            try {
                noticeMapper.createNotice(notice);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //TODO:先匹配jwt中的id与notice中的id，防止篡改他人内容。验证之后才进行更改Notice
    public boolean modifyNoticeWithUser(Notice notice, Long currentUserId){
        try {
            long creatorId = noticeMapper.findNoticeById(notice.getNoticeId()).getCreatorId();

            if(creatorId != currentUserId)
                return false;
            noticeMapper.updateNotice(notice);
            return true;
        } catch (PSQLException e) {
            throw new RuntimeException(e);
        }
    }


//    public void createNotice(String title,String content,Integer creatorId){
//        try {
//            noticeMapper.createNotice(title, content,creatorId);
//        } catch (PSQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public boolean stuViewNotice(long notice_id, long[] stu_id,long user_id){
        //多个学生
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            for (long stuId : stu_id) {
                try {
                    noticeMapper.stuViewNotice(notice_id, stuId);
                } catch (PSQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        }
        return false;
    }
    public boolean stu1ViewNotice(long notice_id, long stu_id,long user_id){
        //一个学生
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            try {
                noticeMapper.stuViewNotice(notice_id, stu_id);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
    public boolean deleteStuViewNotice(long notice_id, long stu_id,long user_id){
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            noticeMapper.deleteStuViewNoticeByStu(notice_id, stu_id);
            return true;
        }
        return false;
    }
    public boolean deleteNotice(long notice_id,long user_id){
        long creator_id;
        creator_id = noticeMapper.findCreatorByNotice(notice_id);
        if (user_id == creator_id) {
            noticeMapper.deleteNotice(notice_id);
            noticeMapper.deleteStuViewNoticeByNotice(notice_id);
            return true;
        }
        return false;
    }
}

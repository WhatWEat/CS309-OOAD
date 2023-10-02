package com.example.projecthelper.service;

import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.util.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    //TODO：发布Notice
    public void postNotice(Notice notice){

    }

    //TODO:先匹配jwt中的id与notice中的id，防止篡改他人内容。验证之后才进行更改Notice
    public boolean modifyNoticeWithUser(Notice notice, String jwt){
        return true;
    }

}

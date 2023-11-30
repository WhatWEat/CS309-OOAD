package com.example.projecthelper.entity.NoticeFactory;

import com.example.projecthelper.entity.Notice;

public class InvitationNotice extends Notice {
    public InvitationNotice(){
        super();
        this.type = Type.RECRUITMENT.getValue();
    }
}

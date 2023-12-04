package com.example.projecthelper.entity.NoticeFactory;

import com.example.projecthelper.entity.Notice;

public class RemoveNotice extends Notice {
    public RemoveNotice(){
        super();
        this.type = Type.REMOVE.getValue();
    }
}

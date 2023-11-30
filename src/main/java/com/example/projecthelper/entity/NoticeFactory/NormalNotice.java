package com.example.projecthelper.entity.NoticeFactory;

import com.example.projecthelper.entity.Notice;

public class NormalNotice extends Notice {
    public NormalNotice(){
        super();
        this.setType(Type.NORMAL.getValue());
    }
}

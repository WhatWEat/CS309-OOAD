package com.example.projecthelper.entity.NoticeFactory;

import com.example.projecthelper.entity.Notice;

public class TransferNotice extends Notice {
    public TransferNotice(){
        super();
        this.type = Type.TRANSFER.getValue();
    }
}

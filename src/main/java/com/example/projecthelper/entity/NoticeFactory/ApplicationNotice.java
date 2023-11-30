package com.example.projecthelper.entity.NoticeFactory;

import com.example.projecthelper.entity.Notice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ApplicationNotice extends Notice {
    public ApplicationNotice(){
        super();
        this.type = Type.APPLICATION.getValue();
    }
}

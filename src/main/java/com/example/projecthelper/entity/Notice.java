package com.example.projecthelper.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {
    private long noticeId;

    private String title;
    private String content;
    private long creatorId;

    public Notice(long noticeId, String title, String content, long creatorId) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.creatorId = creatorId;
    }

    public Notice(){

    }


}

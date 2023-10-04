package com.example.projecthelper.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notice {
    private final long notice_id;

    private String title;
    private String content;
    private final long creator_id;

    public Notice(long notice_id, String title, String content, long creator_id) {
        this.notice_id = notice_id;
        this.title = title;
        this.content = content;
        this.creator_id = creator_id;
    }


}

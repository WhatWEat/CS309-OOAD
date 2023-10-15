package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {
    @TableField("notice_id")
    private long noticeId;
    private String title;
    private String content;
    @TableField("creator_id")
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

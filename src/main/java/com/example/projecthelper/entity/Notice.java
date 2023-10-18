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
    @TableField("project_id")
    private long projectId;
    private String title;
    private String content;
    @TableField("creator_id")
    private long creatorId;

    public Notice(long noticeId, long projectId, String title, String content, long creatorId) {
        this.noticeId = noticeId;
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.creatorId = creatorId;
    }

    public Notice(){
    }


}

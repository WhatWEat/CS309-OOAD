package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {
    private Long noticeId;
    private Long projectId;
    private String title;
    private String content;
    private Long creatorId;

    @TableField(exist = false)
    private List<Long> stuView;

    @TableField(exist = false)
    private Boolean toAll = false;

    public Notice(Long noticeId, Long projectId, String title, String content, Long creatorId) {
        this.noticeId = noticeId;
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.creatorId = creatorId;
    }

    public Notice(){
    }


}

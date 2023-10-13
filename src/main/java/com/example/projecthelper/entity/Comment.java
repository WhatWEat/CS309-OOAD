package com.example.projecthelper.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    @TableField("comment_id")
    private  long commentId;
    @TableField("comment_group")
    private  long commentGroup;
    @TableField("commented_group")
    private  long commentedGroup;

    private  long grade;
    @TableField("comment_time")
    private  Timestamp commentTime;

    private  String content;

    public Comment(long commentId, long commentGroup, long commentedGroup,
                   long grade, Timestamp commentTime, String content) {
        this.commentId = commentId;
        this.commentGroup = commentGroup;
        this.commentedGroup = commentedGroup;
        this.grade = grade;
        this.commentTime = commentTime;
        this.content = content;
    }

    public Comment() {
    }
}

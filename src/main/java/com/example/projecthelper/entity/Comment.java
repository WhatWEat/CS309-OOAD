package com.example.projecthelper.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private final long comment_id;
    private final long comment_group;

    private final long commented_group;

    private final long grade;

    private final Timestamp comment_time;

    private final String content;

    public Comment(long comment_id, long comment_group, long commented_group,
                   long grade, Timestamp comment_time, String content) {
        this.comment_id = comment_id;
        this.comment_group = comment_group;
        this.commented_group = commented_group;
        this.grade = grade;
        this.comment_time = comment_time;
        this.content = content;
    }

}

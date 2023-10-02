package com.example.projecthelper.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private final Integer comment_id;
    private final Integer comment_group;

    private final Integer commented_group;

    private final Integer grade;

    private final Timestamp comment_time;

    private final String content;

    public Comment(Integer comment_id, Integer comment_group, Integer commented_group, Integer grade, Timestamp comment_time, String content) {
        this.comment_id = comment_id;
        this.comment_group = comment_group;
        this.commented_group = commented_group;
        this.grade = grade;
        this.comment_time = comment_time;
        this.content = content;
    }

}

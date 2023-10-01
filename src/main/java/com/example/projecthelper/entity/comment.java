package com.example.projecthelper.entity;

import java.sql.Timestamp;

public class comment {
    private final Integer comment_id;
    private final Integer comment_group;

    private final Integer commented_group;

    private final Integer grade;

    private final Timestamp comment_time;

    private final String content;

    public comment(Integer comment_id, Integer comment_group, Integer commented_group, Integer grade, Timestamp comment_time, String content) {
        this.comment_id = comment_id;
        this.comment_group = comment_group;
        this.commented_group = commented_group;
        this.grade = grade;
        this.comment_time = comment_time;
        this.content = content;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public Integer getComment_group() {
        return comment_group;
    }

    public Integer getCommented_group() {
        return commented_group;
    }

    public Integer getGrade() {
        return grade;
    }

    public Timestamp getComment_time() {
        return comment_time;
    }

    public String getContent() {
        return content;
    }
}

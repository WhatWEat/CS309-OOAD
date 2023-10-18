package com.example.projecthelper.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Evaluation {
    @TableField("evaluation_id")
    private  long evaluationId;
    @TableField("project_id")
    private long projectId;
    @TableField("comment_group")
    private  long commentGroup;
    @TableField("commented_group")
    private  long commentedGroup;
    private  float grade;
    @TableField("comment_time")
    private  Timestamp commentTime;
    private  String content;
    @TableField("submit_id")
    private long submitId;

    public Evaluation(long evaluationId, long projectId, long commentGroup, long commentedGroup,
                      float grade, Timestamp commentTime, String content, long submitId) {
        this.evaluationId = evaluationId;
        this.projectId = projectId;
        this.commentGroup = commentGroup;
        this.commentedGroup = commentedGroup;
        this.grade = grade;
        this.commentTime = commentTime;
        this.content = content;
        this.submitId = submitId;
    }

    public Evaluation() {
    }
}

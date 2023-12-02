package com.example.projecthelper.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Evaluation {
    private  Long evaluationId;
    private Long projectId;
    private  Long commentGroup;
    private  Long commentedGroup;
    private  Float grade;
    private  Timestamp commentTime;
    private  String content;
    private Long assignmentId;

    public Evaluation(long evaluationId, long projectId, long commentGroup, long commentedGroup,
                      float grade, Timestamp commentTime, String content, long assignmentId) {
        this.evaluationId = evaluationId;
        this.projectId = projectId;
        this.commentGroup = commentGroup;
        this.commentedGroup = commentedGroup;
        this.grade = grade;
        this.commentTime = commentTime;
        this.content = content;
        this.assignmentId = assignmentId;
    }

    public Evaluation() {
    }
}

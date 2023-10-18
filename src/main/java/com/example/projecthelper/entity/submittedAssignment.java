package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class submittedAssignment {
    @TableField("submit_id")
    private long submitId;
    @TableField("assignment_id")
    private long assignmentId;
    private float grade;
    @TableField("project_id")
    private long projectId;
    private String text;
    private String comment;
    private String filepath;

    public submittedAssignment(long submitId, long assignmentId, long projectId) {
        this.submitId = submitId;
        this.assignmentId = assignmentId;
        this.projectId = projectId;
    }

    public submittedAssignment() {
    }
}

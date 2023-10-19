package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class submittedAssignment {
    private Long submitId;
    private Long assignmentId;
    private Float grade;
    private Long projectId;
    private String text;
    private String comment;
    private String filepath;

    public submittedAssignment(Long submitId, Long assignmentId, Long projectId) {
        this.submitId = submitId;
        this.assignmentId = assignmentId;
        this.projectId = projectId;
    }

    public submittedAssignment() {
    }
}

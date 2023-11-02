package com.example.projecthelper.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmittedAssignment {
    private Long submitId;
    private Long assignmentId;
    private Float grade;
    private Long projectId;
    private String text;
    private String comment;
    private String filepath;
    private String review;
    public SubmittedAssignment(Long submitId, Long assignmentId, Long projectId) {
        this.submitId = submitId;
        this.assignmentId = assignmentId;
        this.projectId = projectId;
    }

    public SubmittedAssignment(Long assignmentId, Long projectId, String text, String filepath) {
        this.assignmentId = assignmentId;
        this.projectId = projectId;
        this.text = text;
        this.filepath = filepath;
    }

    public SubmittedAssignment() {
    }
}

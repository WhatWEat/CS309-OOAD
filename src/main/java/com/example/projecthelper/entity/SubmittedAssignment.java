package com.example.projecthelper.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmittedAssignment {
    private Long submitId;
    private Long assignmentId;
    private Float grade;
    private Long submitterId;
    private String text;
    private String comment;
    private String filepath;
    private String review;
    private LocalDateTime submitTime;
    public SubmittedAssignment(Long submitId, Long assignmentId, Long submitterId) {
        this.submitId = submitId;
        this.assignmentId = assignmentId;
        this.submitterId = submitterId;
    }

    public SubmittedAssignment(Long assignmentId, Long submitterId, String text, String filepath) {
        this.assignmentId = assignmentId;
        this.submitterId = submitterId;
        this.text = text;
        this.filepath = filepath;
    }

    public SubmittedAssignment() {
    }
}

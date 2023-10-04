package com.example.projecthelper.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmittedAssignment {
    private final long submit_id;

    private final long assignment_id;

    private long grade;

    private String comment;

    private final String text;

    private String filepath;

    public SubmittedAssignment(long submit_id, long assignment_id, String text, String filepath) {
        this.submit_id = submit_id;
        this.assignment_id = assignment_id;
        this.text = text;
        this.filepath = filepath;
    }
}
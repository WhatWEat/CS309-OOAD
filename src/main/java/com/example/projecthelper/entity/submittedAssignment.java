package com.example.projecthelper.entity;

public class submittedAssignment {
    private final Integer submit_id;

    private final Integer assignment_id;

    private Integer grade;

    private String comment;

    private final String text;

    private String filepath;

    public submittedAssignment(Integer submit_id, Integer assignment_id, String text, String filepath) {
        this.submit_id = submit_id;
        this.assignment_id = assignment_id;
        this.text = text;
        this.filepath = filepath;
    }

    public Integer getSubmit_id() {
        return submit_id;
    }

    public Integer getAssignment_id() {
        return assignment_id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getText() {
        return text;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}

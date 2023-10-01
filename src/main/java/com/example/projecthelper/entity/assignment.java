package com.example.projecthelper.entity;

import jakarta.persistence.criteria.CriteriaBuilder;

public class assignment {
    private final Integer assignment_id;
    private String title;
    private String description;

    private final String type;
    private final Integer creator_id;

    public assignment(Integer assignment_id, String title, String description, String type, Integer creator_id) {
        this.assignment_id = assignment_id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.creator_id = creator_id;
    }

    public Integer getAssignment_id() {
        return assignment_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public Integer getCreator_id() {
        return creator_id;
    }
}

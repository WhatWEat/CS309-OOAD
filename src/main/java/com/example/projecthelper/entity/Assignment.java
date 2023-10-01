package com.example.projecthelper.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assignment {
    private final Integer assignment_id;
    private String title;
    private String description;

    private final String type;
    private final Integer creator_id;

    public Assignment(Integer assignment_id, String title, String description, String type, Integer creator_id) {
        this.assignment_id = assignment_id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.creator_id = creator_id;
    }

}

package com.example.projecthelper.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assignment {
    private final long assignment_id;
    private String title;
    private String description;

    private final String type;
    private final long creator_id;

    public Assignment(long assignment_id, String title, String description, String type, long creator_id) {
        this.assignment_id = assignment_id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.creator_id = creator_id;
    }

}

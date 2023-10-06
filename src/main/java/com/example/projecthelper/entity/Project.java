package com.example.projecthelper.entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {
    private final long project_id;

    private String name;

    public Project(long project_id, String name) {
        this.project_id = project_id;
        this.name = name;
    }
}

package com.example.projecthelper.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {
    private final Integer group_id;
    private Integer leader_id;

    private String group_name;
    private final Integer instructor_id;

    private Integer maxsize;

    public Group(Integer group_id, Integer leader_id, String group_name, Integer instructor_id, Integer maxsize) {
        this.group_id = group_id;
        this.leader_id = leader_id;
        this.group_name = group_name;
        this.instructor_id = instructor_id;
        this.maxsize = maxsize;
    }

}

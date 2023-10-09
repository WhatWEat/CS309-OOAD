package com.example.projecthelper.entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {
    private final long group_id;

    private String group_name;
    private final long instructor_id;

    private long maxsize;

    public Group(long group_id,  String group_name, long instructor_id, long maxsize) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.instructor_id = instructor_id;
        this.maxsize = maxsize;
    }

}

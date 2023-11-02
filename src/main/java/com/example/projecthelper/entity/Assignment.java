package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assignment {
    private Long assignmentId;
    private long projectID;
    private String title;
    private int fullMark;
    private String description;
    private String type;
    private Long creatorId;

    public Assignment(Long assignmentId,Long projectID, String title, int fullMark, String description, String type, Long creatorId) {
        this.assignmentId = assignmentId;
        this.projectID = projectID;
        this.title = title;
        this.fullMark = fullMark;
        this.description = description;
        this.type = type;
        this.creatorId = creatorId;
    }

    public Assignment() {
    }
}

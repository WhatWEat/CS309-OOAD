package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assignment {
    @TableField("assignment_id")
    private long assignmentId;
    private String title;
    private String description;
    private String type;
    @TableField("creator_id")
    private long creatorId;

    public Assignment(long assignmentId, String title, String description, String type, long creatorId) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.creatorId = creatorId;
    }

    public Assignment() {
    }
}

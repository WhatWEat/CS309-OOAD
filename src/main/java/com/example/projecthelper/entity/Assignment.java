package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assignment {
    private Long assignmentId;
    private long projectId;
    private String title;
    private int fullMark;
    private String description;
    private String type;
    private Long creatorId;
    private LocalDateTime deadline;

    public Assignment(Long assignmentId,Long projectId, String title, int fullMark, String description, String type, Long creatorId) {
        this.assignmentId = assignmentId;
        this.projectId = projectId;
        this.title = title;
        this.fullMark = fullMark;
        this.description = description;
        this.type = type;
        this.creatorId = creatorId;
    }

    public Assignment() {
    }
}

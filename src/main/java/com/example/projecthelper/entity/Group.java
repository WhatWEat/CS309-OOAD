package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Group {
    private Long groupId;
    private String groupName;
    private Long creatorId;
    private Long instructorId;
    private Long leaderId;
    private Long maxsize;
    private Long projectId;
    private LocalDateTime teamTime;
    private LocalDateTime reportTime;
    private String description;
    public Group(Long groupId,Long instructorId, String groupName, Long maxsize, Long projectId) {
        this.groupId = groupId;
        this.instructorId = instructorId;
        this.groupName = groupName;
        this.maxsize = maxsize;
        this.projectId = projectId;
    }

    public Group() {
    }
}

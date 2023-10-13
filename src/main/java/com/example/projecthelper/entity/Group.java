package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Group {
    @TableField("group_id")
    private  long groupId;
    @TableField("group_name")
    private String groupName;
    @TableField("max_size")
    private long maxsize;
    @TableField("project_id")
    private long projectId;
    @TableField("team_time")
    private Timestamp teamTime;
    private Timestamp deadline;
    public Group(long groupId, String groupName, long maxsize, long projectId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.maxsize = maxsize;
        this.projectId = projectId;
    }

    public Group() {
    }
}

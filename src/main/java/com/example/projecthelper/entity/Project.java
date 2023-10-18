package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {

    @TableField("project_id")
    private long projectId;
    private String description;
    private String name;
    @TableField("teacher_id")
    private long teacherId;

    public Project(long projectId, String name, long teacherId) {
        this.teacherId = teacherId;
        this.name = name;
        this.projectId = projectId;
    }

    public Project() {
    }
}

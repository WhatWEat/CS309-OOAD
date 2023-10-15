package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {

    @TableField("project_id")
    private long projectId;

    private String name;

    public Project(long projectId, String name) {
        this.projectId = projectId;
        this.name = name;
    }

    public Project() {
    }
}

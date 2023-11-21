package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {

    private Long projectId;
    private String description;
    private String name;
    private Long teacherId;
    @TableField(exist = false)
    private String teacherName;

    public Project(Long projectId, String name, Long teacherId) {
        this.teacherId = teacherId;
        this.name = name;
        this.projectId = projectId;
    }

    public Project() {
    }
}

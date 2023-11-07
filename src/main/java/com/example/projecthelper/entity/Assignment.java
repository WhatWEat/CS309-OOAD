package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private LocalDateTime releaseTime;
    private List<String> filePaths;
    private String requireExtension;
    @TableField(exist = false)
    private String projectName;
    @TableField(exist = false)
    private String creatorName;
    @TableField(exist = false)
    private List<MultipartFile> files;


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

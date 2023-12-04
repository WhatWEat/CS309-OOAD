package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @TableField(exist = false)
    private List<MultipartFile> files;
    @TableField(exist = false)
    private int state;


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

    @Getter
    public enum AssignmentState {
        IN_PROGRESS(0),
        SUBMITTED(1),
        RETURNED(2),
        OVERDUE(3),
        UNDEFINED(4);
        private int value;
        AssignmentState(int i){
            value = i;
        }
        public static AssignmentState getState(Assignment assignment, SubmittedAssignment submittedAssignment){
            if(assignment == null)
                return UNDEFINED;
            else if(submittedAssignment == null && assignment.getDeadline().isAfter(LocalDateTime.now()))
                return IN_PROGRESS;
            else if(submittedAssignment == null)
                return OVERDUE;
            else if(submittedAssignment.getGrade() == null)
                return SUBMITTED;
            else
                return RETURNED;
        }
    }
}
